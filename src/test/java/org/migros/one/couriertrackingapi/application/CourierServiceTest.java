package org.migros.one.couriertrackingapi.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.application.strategy.DistanceCalculationStrategyService;
import org.migros.one.couriertrackingapi.domain.distance.DistanceUnitConverter;
import org.migros.one.couriertrackingapi.domain.distance.MeterConverter;
import org.migros.one.couriertrackingapi.domain.entity.CourierLocation;
import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.command.CourierLocationCommand;
import org.migros.one.couriertrackingapi.domain.model.dto.CourierLocationDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.PageableStoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.StoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.TotalTravelDistanceDTO;
import org.migros.one.couriertrackingapi.domain.model.enums.DistanceUnit;
import org.migros.one.couriertrackingapi.domain.model.mapper.ModelMapper;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.migros.one.couriertrackingapi.infrastructure.config.AppConfig;
import org.migros.one.couriertrackingapi.infrastructure.persistence.CourierLocationRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {
    @InjectMocks
    private CourierService sut;

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private DistanceCalculationStrategyService distanceCalculationStrategyService;

    @Mock
    private AppConfig appConfig;

    @Mock
    private ModelMapper mapper;

    private final String courierId = "courier-123";

    @BeforeEach
    void setup() {
        lenient().when(appConfig.getMaxThreshold()).thenReturn(100L);
        lenient().when(appConfig.getPeriodThreshold()).thenReturn(1L);
        lenient().when(appConfig.getEarthRadius()).thenReturn(6371000.0);
    }

    @Test
    void upsertLocation_shouldSaveOnlyValidEntries() {
        CourierLocationCommand command = createCommand();


        StoreDTO store1 = createStoreDTO(1L, 41.001, 29.001);
        StoreDTO store2 = createStoreDTO(2L, 42.0, 30.0);

        PageableStoreDTO pageableStoreDTO = PageableStoreDTO.builder()
                .content(List.of(store1, store2))
                .totalPage(1)
                .build();

        when(storeService.getAllStores(any(PageableQuery.class))).thenReturn(pageableStoreDTO);

        when(distanceCalculationStrategyService.calculation(any()))
                .thenAnswer(invocation -> {
                    DistanceCalculationVO vo = invocation.getArgument(0);
                    return vo.getSecondLatitude().equals(store1.getLat()) ? 50.0 : 200.0;
                });

        when(courierLocationRepository.findTopByCourierIdAndStoreIdOrderByTrackingEntryDateDesc(eq(courierId), eq(1L)))
                .thenReturn(Optional.empty());
        when(courierLocationRepository.findTopByCourierIdAndStoreIdOrderByTrackingEntryDateDesc(eq(courierId), eq(2L)))
                .thenReturn(Optional.empty());

        when(mapper.storeDTOToStore(store1)).thenReturn(Store.builder().id(1L).build());
        when(mapper.storeDTOToStore(store2)).thenReturn(Store.builder().id(2L).build());

        List<CourierLocation> savedTracking = new ArrayList<>();
        doAnswer(invocation -> {
            savedTracking.addAll(invocation.getArgument(0));
            return null;
        }).when(courierLocationRepository).saveAll(anyList());

        when(mapper.courierTrackingListToCourierTrackingDTO(anyList()))
                .thenAnswer(invocation -> {
                    List<CourierLocation> list = invocation.getArgument(0);
                    List<CourierLocationDTO> dtoList = new ArrayList<>();
                    for (CourierLocation ct : list) {
                        dtoList.add(CourierLocationDTO.builder().build());
                    }
                    return dtoList;
                });

        List<CourierLocationDTO> result = sut.upsertLocation(command, courierId);

        assertThat(savedTracking).hasSize(1);
        assertThat(savedTracking.get(0).getStore().getId()).isEqualTo(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getTotalTravelDistance_returnsZero_whenLessThanTwoLocations() {
        when(courierLocationRepository.findByCourierIdOrderByTrackingEntryDateAsc(courierId))
                .thenReturn(List.of(CourierLocation.builder().latitude(41.0).longitude(29.0).build()));

        when(mapper.totalTravelDistanceToTotalTravelDistanceDTO(0.0, DistanceUnit.METERS.getUnit()))
                .thenReturn(TotalTravelDistanceDTO.builder().travelDistance(0.0).distanceUnit(DistanceUnit.METERS.getUnit()).build());

        TotalTravelDistanceDTO dto = sut.getTotalTravelDistance(courierId);

        assertThat(dto.getTravelDistance()).isEqualTo(0.0);
        assertThat(dto.getDistanceUnit()).isEqualTo(DistanceUnit.METERS.getUnit());
    }

    @Test
    void getTotalTravelDistance_calculatesCorrectly_withMultipleLocations() {
        LocalDateTime now = LocalDateTime.now();

        CourierLocation ct1 = CourierLocation.builder()
                .latitude(41.0)
                .longitude(29.0)
                .trackingEntryDate(now)
                .build();

        CourierLocation ct2 = CourierLocation.builder()
                .latitude(41.1)
                .longitude(29.1)
                .trackingEntryDate(now.plusMinutes(1))
                .build();

        // Mock: Repository courier locations
        when(courierLocationRepository.findByCourierIdOrderByTrackingEntryDateAsc(courierId))
                .thenReturn(List.of(ct1, ct2));

        when(appConfig.getDistanceUnit()).thenReturn("m");

        when(distanceCalculationStrategyService.calculation(any())).thenReturn(1500.0);

        DistanceUnitConverter converter = new MeterConverter();
        String expectedUnit = converter.getUnit();

        when(mapper.totalTravelDistanceToTotalTravelDistanceDTO(1500.0, expectedUnit))
                .thenReturn(TotalTravelDistanceDTO.builder()
                        .travelDistance(1500.0)
                        .distanceUnit(expectedUnit)
                        .build());

        TotalTravelDistanceDTO dto = sut.getTotalTravelDistance(courierId);

        assertThat(dto.getTravelDistance()).isEqualTo(1500.0);
        assertThat(dto.getDistanceUnit()).isEqualTo(expectedUnit);

        verify(distanceCalculationStrategyService, times(1)).calculation(any());
        verify(courierLocationRepository).findByCourierIdOrderByTrackingEntryDateAsc(courierId);
        verify(mapper).totalTravelDistanceToTotalTravelDistanceDTO(1500.0, expectedUnit);
    }

    private CourierLocationCommand createCommand() {
        return CourierLocationCommand.builder()
                .lat(41.0)
                .lng(29.0)
                .trackingEntryDate(LocalDateTime.now())
                .build();
    }

    private StoreDTO createStoreDTO(Long id, double lat, double lng) {
        return StoreDTO.builder()
                .id(id)
                .lat(lat)
                .lng(lng)
                .build();
    }
}