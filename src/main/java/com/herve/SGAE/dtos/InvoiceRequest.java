package com.herve.SGAE.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private BigDecimal amount;
    private LocalDate dateDue;
    private Long studentId;
}
