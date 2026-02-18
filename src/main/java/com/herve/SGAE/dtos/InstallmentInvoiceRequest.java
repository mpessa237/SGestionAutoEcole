package com.herve.SGAE.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentInvoiceRequest {
    private Long studentId;
    private int numberOfInstallments;
}
