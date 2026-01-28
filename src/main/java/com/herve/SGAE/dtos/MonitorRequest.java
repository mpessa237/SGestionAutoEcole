package com.herve.SGAE.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitorRequest extends UserRequest{
    private String speciality;
    private String phoneNumber;

}
