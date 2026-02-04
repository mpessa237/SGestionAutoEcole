package com.herve.SGAE.services;

import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.dtos.PermitInvoiceRequest;
import com.herve.SGAE.enums.StatusInvoice;
import com.herve.SGAE.mappers.InvoiceMapper;
import com.herve.SGAE.models.Invoice;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.repository.InvoiceRepo;
import com.herve.SGAE.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service

public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final InvoiceMapper invoiceMapper;
    private final StudentRepo studentRepo;
    private final InvoiceService invoiceService;

    public InvoiceService(InvoiceRepo invoiceRepo, InvoiceMapper invoiceMapper, StudentRepo studentRepo,@Lazy InvoiceService invoiceService) {
        this.invoiceRepo = invoiceRepo;
        this.invoiceMapper = invoiceMapper;
        this.studentRepo = studentRepo;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public InvoiceResponse generateInvoice(InvoiceRequest invoiceRequest) {
        Student student = studentRepo.findById(invoiceRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

    @Transactional
    public InvoiceResponse generatePermitInvoice(PermitInvoiceRequest permitInvoiceRequest) {
        Student student = studentRepo.findById(permitInvoiceRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found!"));

        // permet de recuperer le prix de la categorie de permis
        BigDecimal permitPrice = new BigDecimal(student.getPermitCategory().getPrice());

        // Génère une facture pour la catégorie de permis (payable en 4 tranches)
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(permitPrice);
        invoiceRequest.setDateDue(LocalDate.now().plusMonths(4));
        invoiceRequest.setStudentId(student.getId());
        invoiceRequest.setNumberOfInstallments(4);
        invoiceRequest.setPermitCategory(student.getPermitCategory());

        return invoiceService.generateInvoice(invoiceRequest);
    }


    @Transactional
    public InvoiceResponse payInstallment(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (invoice.getPaidInstallments() >= invoice.getNumberOfInstallments()) {
            throw new IllegalStateException("All installments have already been paid.");
        }
        invoice.setAmountPaid(invoice.getAmountPaid().add(invoice.getInstallmentAmount()));
        invoice.setPaidInstallments(invoice.getPaidInstallments() + 1);

        if (invoice.getAmountPaid().compareTo(invoice.getAmount()) >= 0) {
            invoice.setStatusInvoice(StatusInvoice.PAID);
        }

        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(updatedInvoice);
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
