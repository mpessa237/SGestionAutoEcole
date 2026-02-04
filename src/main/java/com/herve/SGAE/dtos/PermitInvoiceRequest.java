package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.PermitCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermitInvoiceRequest {
    private Long studentId;
    private PermitCategory permitCategory;
}
