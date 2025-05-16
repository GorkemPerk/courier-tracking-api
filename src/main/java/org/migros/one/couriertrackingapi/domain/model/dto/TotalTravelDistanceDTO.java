package org.migros.one.couriertrackingapi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalTravelDistanceDTO {
    private Double travelDistance;
    private String distanceUnit;
}
