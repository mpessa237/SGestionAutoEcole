package com.herve.SGAE.mappers;

import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.enums.StatusInvoice;
import com.herve.SGAE.models.Invoice;
import com.herve.SGAE.models.Student;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
public class InvoiceMapper {

    public Invoice toEntity(InvoiceRequest invoiceRequest, Student student){

        Invoice invoice = new Invoice();
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setDateDue(invoiceRequest.getDateDue());
        invoice.setDateEmission(LocalDateTime.now());
        invoice.setStatusInvoice(StatusInvoice.PENDING);
        invoice.setNumberOfInstallments(invoiceRequest.getNumberOfInstallments());
        invoice.setPermitCategory(invoiceRequest.getPermitCategory());
        invoice.setStudent(student);
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.setPaidInstallments(0);

        return invoice;
    }



    public InvoiceResponse toResponse(Invoice invoice){

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setAmount(invoice.getAmount());
        invoiceResponse.setDateDue(invoice.getDateDue());
        invoiceResponse.setDateEmission(invoice.getDateEmission());
        invoiceResponse.setAmountPaid(invoice.getAmountPaid());
        invoiceResponse.setPermitCategory(invoice.getPermitCategory());
        invoiceResponse.setInstallmentAmount(invoice.getInstallmentAmount());
        invoiceResponse.setNumberOfInstallments(invoice.getNumberOfInstallments());
        invoiceResponse.setPaidInstallments(invoice.getPaidInstallments());
        invoiceResponse.setStatusInvoice(invoice.getStatusInvoice());
        invoiceResponse.setAccessStartDate(invoice.getAccessStartDate());
        invoiceResponse.setAccessEndDate(invoice.getAccessEndDate());
        invoiceResponse.setStudentId(invoice.getStudent().getId());
        invoiceResponse.setStudentFirstname(invoice.getStudent().getFirstname());

        return invoiceResponse;

    }
}
