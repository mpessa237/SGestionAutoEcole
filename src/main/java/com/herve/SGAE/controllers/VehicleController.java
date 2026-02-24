package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.VehicleRequest;
import com.herve.SGAE.dtos.VehicleResponse;
import com.herve.SGAE.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(@RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse response = vehicleService.createVehicle(vehicleRequest);
        return ResponseEntity.ok(response);
    }
}
