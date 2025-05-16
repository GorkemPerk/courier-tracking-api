package org.migros.one.couriertrackingapi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableStoreDTO {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalElements;
    private List<StoreDTO> content;
}
