package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.InstallmentInvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InvoiceResponse>>getInvoicesByStudentId(@PathVariable Long studentId) throws AccessDeniedException {
        List<InvoiceResponse> invoiceResponses = invoiceService.getInvoicesByStudentId(studentId);
        return ResponseEntity.ok(invoiceResponses);
    }



    @PutMapping("/{invoiceId}/mark-as-paid")
    public ResponseEntity<InvoiceResponse> markInvoiceAsPaid(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.markInvoiceAsPaid(invoiceId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-installment-invoice")
    public ResponseEntity<InvoiceResponse> generateInstallmentInvoice(@RequestBody InstallmentInvoiceRequest request) {
        InvoiceResponse response = invoiceService.generateInstallmentInvoice(request.getStudentId(), request.getNumberOfInstallments());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{invoiceId}/pay-installment")
    public ResponseEntity<InvoiceResponse> payInstallment(@PathVariable Long invoiceId) {
        InvoiceResponse response = invoiceService.payInstallment(invoiceId);
        return ResponseEntity.ok(response);
    }




}
