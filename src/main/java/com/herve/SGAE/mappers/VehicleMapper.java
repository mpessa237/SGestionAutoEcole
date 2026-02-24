package com.herve.SGAE.mappers;

import com.herve.SGAE.dtos.VehicleRequest;
import com.herve.SGAE.dtos.VehicleResponse;
import com.herve.SGAE.enums.StatusVehicle;
import com.herve.SGAE.models.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequest vehicleRequest) {
        Vehicle vehicle = new Vehicle();
        vehicle.setTypeVehicle(vehicleRequest.getTypeVehicle());
        vehicle.setMarque(vehicleRequest.getMarque());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setRegistration(vehicleRequest.getRegistrationNumber());
        vehicle.setYear(vehicleRequest.getYear());
        vehicle.setStatusVehicle(StatusVehicle.AVAILABLE);

        return vehicle;
    }

    public VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(vehicle.getId());
        vehicleResponse.setTypeVehicle(vehicle.getTypeVehicle());
        vehicleResponse.setMarque(vehicle.getMarque());
        vehicleResponse.setModel(vehicle.getModel());
        vehicleResponse.setRegistrationNumber(vehicle.getRegistration());
        vehicleResponse.setYear(vehicle.getYear());
        vehicleResponse.setStatusVehicle(vehicle.getStatusVehicle());

        return vehicleResponse;
    }
}
