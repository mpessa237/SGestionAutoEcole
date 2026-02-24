package com.herve.SGAE.services;

import com.herve.SGAE.dtos.VehicleRequest;
import com.herve.SGAE.dtos.VehicleResponse;
import com.herve.SGAE.mappers.VehicleMapper;
import com.herve.SGAE.models.Vehicle;
import com.herve.SGAE.repository.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepo vehicleRepo;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse createVehicle(VehicleRequest vehicleRequest){
        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequest);
        Vehicle savedvehicle = vehicleRepo.save(vehicle);
        return vehicleMapper.toResponse(savedvehicle);
    }
}
