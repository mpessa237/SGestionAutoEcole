package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/initial/{studentId}")
    public ResponseEntity<InvoiceResponse> generateInitialInvoice(@PathVariable Long studentId) {
        InvoiceResponse response = invoiceService.generateInitialInvoice(studentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{invoiceId}/pay-initial")
    public ResponseEntity<InvoiceResponse> payInitialInvoice(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.payInitialInvoice(invoiceId);
        return ResponseEntity.ok(response);
    }



}
