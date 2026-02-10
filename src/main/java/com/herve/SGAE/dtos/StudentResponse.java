package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.PermitCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    //private String password;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private PermitCategory permitCategory;
}
