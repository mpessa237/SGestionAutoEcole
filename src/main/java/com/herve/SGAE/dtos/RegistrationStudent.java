package com.herve.SGAE.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationStudent {
    private StudentResponse studentResponse;
    private Long registrationInvoiceId;
    private Long permitInvoiceId;
}
