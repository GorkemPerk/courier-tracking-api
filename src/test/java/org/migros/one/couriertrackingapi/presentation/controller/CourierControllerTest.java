package org.migros.one.couriertrackingapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.migros.one.couriertrackingapi.application.CourierService;
import org.migros.one.couriertrackingapi.domain.model.command.CourierLocationCommand;
import org.migros.one.couriertrackingapi.domain.model.dto.CourierLocationDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.TotalTravelDistanceDTO;
import org.migros.one.couriertrackingapi.domain.model.enums.DistanceUnit;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourierService courierService;

    @InjectMocks
    private CourierController courierController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final String BASE_URL = "/api/v1/couriers";
    private static final String COURIER_ID = "courier-123";

    private CourierLocationCommand command;
    private CourierLocationDTO trackingDTO;
    private TotalTravelDistanceDTO distanceDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courierController).build();

        command = CourierLocationCommand.builder()
                .lat(41.0)
                .lng(29.0)
                .trackingEntryDate(LocalDateTime.now())
                .build();

        trackingDTO = CourierLocationDTO.builder()
                .storeId(1L)
                .latitude(41.0)
                .longitude(29.0)
                .trackingEntryDate(LocalDateTime.now())
                .build();

        distanceDTO = TotalTravelDistanceDTO.builder()
                .travelDistance(1500.0)
                .distanceUnit(DistanceUnit.METERS.getUnit())
                .build();
    }

    @Test
    void upsertLocation_shouldReturnCreated() throws Exception {
        when(courierService.upsertLocation(command, COURIER_ID))
                .thenReturn(List.of(trackingDTO));

        mockMvc.perform(post(BASE_URL + "/tracking/{courierId}", COURIER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].storeId").value(1L));
    }

    @Test
    void getTotalTravelDistance_shouldReturnOk() throws Exception {
        when(courierService.getTotalTravelDistance(COURIER_ID)).thenReturn(distanceDTO);

        mockMvc.perform(get(BASE_URL + "/tracking-distance/{courierId}", COURIER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.travelDistance").value(1500.0))
                .andExpect(jsonPath("$.data.distanceUnit").value(DistanceUnit.METERS.getUnit()));
    }
}