package org.migros.one.couriertrackingapi.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.migros.one.couriertrackingapi.application.strategy.DistanceCalculationStrategyService;
import org.migros.one.couriertrackingapi.domain.distance.DistanceUnitConverter;
import org.migros.one.couriertrackingapi.domain.distance.DistanceUnitConverterFactory;
import org.migros.one.couriertrackingapi.domain.entity.CourierLocation;
import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.command.CourierLocationCommand;
import org.migros.one.couriertrackingapi.domain.model.dto.CourierLocationDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.StoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.TotalTravelDistanceDTO;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;
import org.migros.one.couriertrackingapi.domain.model.enums.DistanceUnit;
import org.migros.one.couriertrackingapi.domain.model.mapper.ModelMapper;
import org.migros.one.couriertrackingapi.domain.model.vo.DistanceCalculationVO;
import org.migros.one.couriertrackingapi.infrastructure.config.AppConfig;
import org.migros.one.couriertrackingapi.infrastructure.persistence.CourierLocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {
    private final CourierLocationRepository courierLocationRepository;
    private final StoreService storeService;
    private final DistanceCalculationStrategyService distanceCalculationStrategyService;
    private final AppConfig appConfig;
    private final ModelMapper mapper;

    public List<CourierLocationDTO> upsertLocation(CourierLocationCommand command, String courierId) {
        List<CourierLocation> courierLocations = storeService.getAllStores(PageableQuery.builder().build()).getContent().stream()
                .map(store -> createCourierTracking(store, command, courierId))
                .toList();

        List<CourierLocation> validCourierLocations = new ArrayList<>();
        for (CourierLocation tracking : courierLocations) {
            if (isValidEntry(courierId, tracking)) {
                validCourierLocations.add(tracking);
            } else {
                log.warn("Invalid courier entry filtered out: courierId={}, storeId={}, distance={}, trackingEntryDate={}",
                        courierId, tracking.getStore().getId(), tracking.getDistance(), tracking.getTrackingEntryDate());
            }
        }

        courierLocationRepository.saveAll(validCourierLocations);
        List<CourierLocationDTO> courierLocationDTO = mapper.courierTrackingListToCourierTrackingDTO(validCourierLocations);
        log.info("Saved valid couriers entrance: {}", courierLocationDTO);
        return courierLocationDTO;
    }

    private boolean isValidEntry(String courierId, CourierLocation courierLocation) {
        boolean isWithinDistanceThreshold = courierLocation.getDistance() <= appConfig.getMaxThreshold();
        boolean isTimeValid = courierLocationRepository
                .findTopByCourierIdAndStoreIdOrderByTrackingEntryDateDesc(courierId, courierLocation.getStore().getId())
                .map(last -> last.getTrackingEntryDate().isBefore(courierLocation.getTrackingEntryDate().minusMinutes(appConfig.getPeriodThreshold())))
                .orElse(true);
        return isWithinDistanceThreshold && isTimeValid;
    }

    private CourierLocation createCourierTracking(StoreDTO storeDTO, CourierLocationCommand command, String courierId) {
        DistanceCalculationVO distanceCalculation = distanceCalculation(command.getLat(), command.getLng(), storeDTO.getLat(), storeDTO.getLng(), CalculationType.HAVERSINE);
        // strategy
        Double distance = distanceCalculationStrategyService.calculation(distanceCalculation);

        // factory
        DistanceUnitConverter converter = DistanceUnitConverterFactory.getConverter(DistanceUnit.METERS.getUnit());
        Double convertedDistance = converter.convert(distance);

        Store store = mapper.storeDTOToStore(storeDTO);

        return CourierLocation.builder()
                .courierId(courierId)
                .store(store)
                .latitude(command.getLat())
                .longitude(command.getLng())
                .distance(convertedDistance)
                .trackingEntryDate(command.getTrackingEntryDate())
                .build();
    }

    private DistanceCalculationVO distanceCalculation(Double firstLatitude, Double firstLongitude, Double secondLatitude, Double secondLongitude, CalculationType calculationType) {
        return DistanceCalculationVO.builder()
                .firstLatitude(firstLatitude)
                .firstLongitude(firstLongitude)
                .secondLatitude(secondLatitude)
                .secondLongitude(secondLongitude)
                .calculationType(calculationType)
                .earthRadius(appConfig.getEarthRadius())
                .build();
    }

    public TotalTravelDistanceDTO getTotalTravelDistance(String courierId) {
        List<CourierLocation> courierLocation = courierLocationRepository.findByCourierIdOrderByTrackingEntryDateAsc(courierId);
        if (courierLocation.size() < 2) {
            log.info("Courier '{}' has only {} location record(s); travel distance calculation skipped.", courierId, courierLocation.size());
            return mapper.totalTravelDistanceToTotalTravelDistanceDTO(0.0, DistanceUnit.METERS.getUnit());
        }
        // strategy
        double totalTravelDistance = IntStream.range(1, courierLocation.size())
                .mapToDouble(i -> distanceCalculationStrategyService.calculation(
                        distanceCalculation(courierLocation.get(i - 1).getLatitude(), courierLocation.get(i - 1).getLongitude(),
                                courierLocation.get(i).getLatitude(), courierLocation.get(i).getLongitude(), CalculationType.COSINES)))
                .sum();
        // factory
        DistanceUnitConverter converter = DistanceUnitConverterFactory.getConverter(appConfig.getDistanceUnit());
        Double convertedDistance = converter.convert(totalTravelDistance);

        return mapper.totalTravelDistanceToTotalTravelDistanceDTO(convertedDistance, converter.getUnit());
    }
}
