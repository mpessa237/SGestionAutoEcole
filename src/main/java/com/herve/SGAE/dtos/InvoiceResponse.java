package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.PermitCategory;
import com.herve.SGAE.enums.StatusInvoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private Long id;
    private LocalDateTime dateEmission;
    private LocalDate dateDue;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private int numberOfInstallments;
    private BigDecimal installmentAmount;
    private LocalDate accessStartDate;
    private LocalDate accessEndDate;
    private int paidInstallments;
    private PermitCategory permitCategory;
    private StatusInvoice statusInvoice;
    private Long studentId;
    private String studentFirstname;
}
