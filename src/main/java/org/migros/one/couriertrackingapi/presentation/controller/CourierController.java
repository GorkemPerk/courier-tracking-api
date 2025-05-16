package org.migros.one.couriertrackingapi.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.migros.one.couriertrackingapi.application.CourierService;
import org.migros.one.couriertrackingapi.domain.model.command.CourierLocationCommand;
import org.migros.one.couriertrackingapi.domain.model.dto.CourierLocationDTO;
import org.migros.one.couriertrackingapi.domain.model.dto.TotalTravelDistanceDTO;
import org.migros.one.couriertrackingapi.presentation.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService courierService;

    @PostMapping("/tracking/{courierId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<List<CourierLocationDTO>> trackLocation(@PathVariable String courierId, @Valid @RequestBody CourierLocationCommand command) {
        List<CourierLocationDTO> courierLocationDTO = courierService.upsertLocation(command, courierId);
        return Response.success(courierLocationDTO);
    }

    @GetMapping("/tracking-distance/{courierId}")
    public Response<TotalTravelDistanceDTO> getTotalTravelDistance(@PathVariable String courierId) {
        TotalTravelDistanceDTO totalTravelDistance = courierService.getTotalTravelDistance(courierId);
        return Response.success(totalTravelDistance);
    }
}
