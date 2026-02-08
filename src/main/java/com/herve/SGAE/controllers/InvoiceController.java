package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.dtos.PermitInvoiceRequest;
import com.herve.SGAE.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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


    @PostMapping("/permit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InvoiceResponse> permitInvoice(@RequestBody PermitInvoiceRequest permitInvoiceRequest){
        InvoiceResponse response = invoiceService.generatePermitInvoice(permitInvoiceRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{invoiceId}/pay-installment")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InvoiceResponse> payInstallment(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.payInstallment(invoiceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByStudent(@PathVariable Long studentId) throws AccessDeniedException {
        List<InvoiceResponse> responses = invoiceService.getInvoiceByStudentId(studentId);

        return ResponseEntity.ok(responses);
    }


}
