package com.herve.SGAE.services;


import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.enums.StatusInvoice;
import com.herve.SGAE.mappers.InvoiceMapper;
import com.herve.SGAE.models.Invoice;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.repository.InvoiceRepo;
import com.herve.SGAE.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final StudentRepo studentRepo;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    public InvoiceResponse generateInitialInvoice(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(new BigDecimal("10000"));
        invoiceRequest.setDateDue(LocalDate.now().plusDays(14));
        invoiceRequest.setStudentId(studentId);
        invoiceRequest.setNumberOfInstallments(1);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        invoice.setAccessStartDate(LocalDate.now());
        invoice.setAccessEndDate(LocalDate.now().plusDays(14));

        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }


    //marque la facture comme payer
    @Transactional
    public InvoiceResponse payInitialInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (invoice.getStatusInvoice() == StatusInvoice.PAID) {
            throw new IllegalStateException("La facture est déjà payée");
        }

        invoice.setAmountPaid(invoice.getAmount());
        invoice.setPaidInstallments(1);
        invoice.setStatusInvoice(StatusInvoice.PAID);

        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(updatedInvoice);
    }




}
