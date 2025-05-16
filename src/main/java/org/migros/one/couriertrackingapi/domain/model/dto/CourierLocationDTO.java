package org.migros.one.couriertrackingapi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationDTO {
    private Long storeId;
    private Double distance;
    private Double latitude;
    private Double longitude;
    private LocalDateTime trackingEntryDate;
}
