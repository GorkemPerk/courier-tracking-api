package org.migros.one.couriertrackingapi.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.application.StoreService;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.dto.PageableStoreDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.StoreDTO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {
    @InjectMocks
    private StoreController sut;

    private MockMvc mockMvc;

    @Mock
    private StoreService storeService;
    private static final String BASE_URL = "/api/v1/stores";

    private List<StoreDTO> storeDTOList;
    private PageableStoreDTO pageableStoreDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sut).build();

        storeDTOList = List.of(
                StoreDTO.builder().id(1L).lat(41.0).lng(29.0).build(),
                StoreDTO.builder().id(2L).lat(42.0).lng(30.0).build()
        );

        pageableStoreDTO = PageableStoreDTO.builder()
                .content(storeDTOList)
                .build();

    }

    @Test
    void getAllStores_shouldReturnStoreListSuccessfully() throws Exception {
        when(storeService.getAllStores(any(PageableQuery.class))).thenReturn(pageableStoreDTO);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].lat").value(41.0))
                .andExpect(jsonPath("$.data.content[0].lng").value(29.0))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].lat").value(42.0))
                .andExpect(jsonPath("$.data.content[1].lng").value(30.0))
                .andExpect(jsonPath("$.error").doesNotExist());

        verify(storeService, times(1)).getAllStores(any(PageableQuery.class));
    }
}