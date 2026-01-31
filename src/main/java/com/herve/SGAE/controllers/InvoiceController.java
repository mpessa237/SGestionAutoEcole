package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InvoiceResponse> generate(@RequestBody InvoiceRequest invoiceRequest){
        InvoiceResponse response = invoiceService.generateInvoice(invoiceRequest);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{invoiceId}/pay")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InvoiceResponse> markInvoiceAsPaid(@PathVariable Long invoiceId){

        InvoiceResponse response = invoiceService.markInvoiceAsPaid(invoiceId);
        return ResponseEntity.ok(response);
    }
}
