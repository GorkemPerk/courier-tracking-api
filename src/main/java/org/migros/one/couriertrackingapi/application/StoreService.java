package org.migros.one.couriertrackingapi.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.migros.one.couriertrackingapi.domain.model.PageableQuery;
import org.migros.one.couriertrackingapi.domain.model.dto.PageableStoreDTO;
import org.migros.one.couriertrackingapi.domain.model.mapper.ModelMapper;
import org.migros.one.couriertrackingapi.infrastructure.persistence.StoreRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
    private final ModelMapper mapper;

    @PostConstruct
    public void loadStoresFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("stores.json");
            List<Store> stores = mapper.readValue(is, new TypeReference<>() {
            });
            storeRepository.saveAll(stores);
            log.info("store saved size:{}", stores.size());
        } catch (Exception e) {
            log.error("Error occurred during data initialization: {}", e.getMessage(), e);
        }
    }

    // Singleton
    @Cacheable(value = "storeCache", key = "'page:' + #query.page + ':size:' + #query.size")
    public PageableStoreDTO getAllStores(PageableQuery query) {
        log.info("getAllStores query:{}", query);
        Pageable pageable = PageRequest.of(query.getPage() - 1, query.getSize());
        Page<Store> storePage = storeRepository.findAll(pageable);
        if (!storePage.hasContent()) {
            return PageableStoreDTO.builder()
                    .page(query.getPage())
                    .size(query.getSize())
                    .totalElements(storePage.getTotalElements())
                    .totalPage(storePage.getTotalPages())
                    .content(Collections.emptyList())
                    .build();
        }
        return PageableStoreDTO.builder()
                .page(query.getPage())
                .size(query.getSize())
                .totalElements(storePage.getTotalElements())
                .totalPage(storePage.getTotalPages())
                .content(mapper.storeListToStoreDTOList(storePage.getContent()))
                .build();
    }
}
