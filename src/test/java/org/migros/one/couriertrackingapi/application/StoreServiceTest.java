package org.migros.one.couriertrackingapi.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.dto.PageableStoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.StoreDTO;
import org.migros.one.couriertrackingapi.domain.model.mapper.ModelMapper;
import org.migros.one.couriertrackingapi.infrastructure.persistence.StoreRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @InjectMocks
    private StoreService sut;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ModelMapper mapper;
    private List<Store> storeList;
    private List<StoreDTO> storeDTOList;

    @BeforeEach
    void setUp() {
        storeList = List.of(
                Store.builder().id(1L).name("Store A").lat(41.0).lng(29.0).build(),
                Store.builder().id(2L).name("Store B").lat(42.0).lng(30.0).build()
        );

        storeDTOList = List.of(
                StoreDTO.builder().id(1L).lat(41.0).lng(29.0).build(),
                StoreDTO.builder().id(2L).lat(42.0).lng(30.0).build()
        );
    }

    @Test
    void getAllStores_shouldReturnMappedStoreDTOList() {
        Page<Store> storePage = new PageImpl<>(storeList);
        when(storeRepository.findAll(any(Pageable.class))).thenReturn(storePage);
        when(mapper.storeListToStoreDTOList(storeList)).thenReturn(storeDTOList);

        PageableStoreDTO result = sut.getAllStores(PageableQuery.builder().build());

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getContent().get(1).getId()).isEqualTo(2L);

        verify(storeRepository, times(1)).findAll(any(Pageable.class));
        verify(mapper, times(1)).storeListToStoreDTOList(storeList);
    }
}