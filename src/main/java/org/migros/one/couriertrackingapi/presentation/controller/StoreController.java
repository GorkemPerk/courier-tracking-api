package org.migros.one.couriertrackingapi.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.migros.one.couriertrackingapi.application.StoreService;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.dto.PageableStoreDTO;
import org.migros.one.couriertrackingapi.presentation.model.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public Response<PageableStoreDTO> getAllStores(PageableQuery query) {
        PageableStoreDTO pageableStoreDTO = storeService.getAllStores(query);
        return Response.success(pageableStoreDTO);
    }
}
