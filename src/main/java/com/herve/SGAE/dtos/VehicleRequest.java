package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.TypeVehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {
    private TypeVehicle typeVehicle;
    private String marque;
    private String model;
    private String registrationNumber;
    private Integer year;
}
