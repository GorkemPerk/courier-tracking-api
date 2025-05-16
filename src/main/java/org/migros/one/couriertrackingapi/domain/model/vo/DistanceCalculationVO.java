package org.migros.one.couriertrackingapi.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.migros.one.couriertrackingapi.domain.model.enums.CalculationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistanceCalculationVO {
    private Double firstLatitude;
    private Double firstLongitude;
    private Double secondLatitude;
    private Double secondLongitude;
    private CalculationType calculationType;
    private Double earthRadius;
}
