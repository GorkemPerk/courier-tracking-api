package org.migros.one.couriertrackingapi.domain.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.migros.one.couriertrackingapi.domain.entity.CourierLocation;
import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.migros.one.couriertrackingapi.domain.model.dto.CourierLocationDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.StoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.TotalTravelDistanceDTO;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ModelMapper {

    @Mapping(source = "store.id", target = "storeId")
    CourierLocationDTO courierTrackingToCourierTrackingDTO(CourierLocation courierLocation);

    List<CourierLocationDTO> courierTrackingListToCourierTrackingDTO(List<CourierLocation> courierLocation);

    List<StoreDTO> storeListToStoreDTOList(List<Store> stores);

    Store storeDTOToStore(StoreDTO storeDTO);

    @Mapping(target = "travelDistance", source = "totalTravelDistance")
    @Mapping(target = "distanceUnit", source = "distanceUnit")
    TotalTravelDistanceDTO totalTravelDistanceToTotalTravelDistanceDTO(Double totalTravelDistance, String distanceUnit);

    StoreDTO storeToStoreDTO(Store store);
}
