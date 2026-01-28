package com.herve.SGAE.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest extends UserRequest{
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
}
