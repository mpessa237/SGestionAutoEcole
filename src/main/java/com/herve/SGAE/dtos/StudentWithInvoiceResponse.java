package com.herve.SGAE.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithInvoiceResponse {
    private StudentResponse student;
    private InvoiceResponse registrationInvoice;


}
