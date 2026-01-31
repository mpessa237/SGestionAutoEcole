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

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final InvoiceMapper invoiceMapper;
    private final StudentRepo studentRepo;

    @Transactional
    public InvoiceResponse generateInvoice(InvoiceRequest invoiceRequest) {
        Student student = studentRepo.findById(invoiceRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        Invoice savedInvoice = invoiceRepo.save(invoice);

        return invoiceMapper.toResponse(savedInvoice);
    }

    @Transactional
    public InvoiceResponse markInvoiceAsPaid(Long invoiceId){

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(()-> new IllegalArgumentException("invoice not found!!"));

        invoice.setStatusInvoice(StatusInvoice.PAID);
        Invoice updateInvoice = invoiceRepo.save(invoice);

        return invoiceMapper.toResponse(updateInvoice);
    }

}
