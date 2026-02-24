package com.herve.SGAE.services;


import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.enums.InvoiceType;
import com.herve.SGAE.enums.PermitCategory;
import com.herve.SGAE.enums.StatusInvoice;
import com.herve.SGAE.mappers.InvoiceMapper;
import com.herve.SGAE.models.Invoice;
import com.herve.SGAE.models.Payment;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.repository.InvoiceRepo;
import com.herve.SGAE.repository.StudentRepo;
import com.herve.SGAE.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final StudentRepo studentRepo;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    public InvoiceResponse generateRegistrationInvoice(Long studentId) {
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
        invoice.setInvoiceType(InvoiceType.REGISTRATION);

        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

    @Transactional
    public InvoiceResponse generatePermitInvoice(Long studentId, PermitCategory permitCategory) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(new BigDecimal(permitCategory.getPrice()));
        invoiceRequest.setDateDue(LocalDate.now().plusMonths(4));
        invoiceRequest.setPermitCategory(permitCategory);
        invoiceRequest.setStudentId(studentId);
        invoiceRequest.setNumberOfInstallments(4);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        invoice.setAccessStartDate(LocalDate.now());
        invoice.setAccessEndDate(LocalDate.now().plusMonths(4));
        invoice.setInvoiceType(InvoiceType.PERMIT);

        BigDecimal installmentAmount = invoice.getAmount().divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);
        invoice.setInstallmentAmount(installmentAmount);

        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }


    //pour les paiement en une seul fois comme l'inscription par exemple
    @Transactional
    public InvoiceResponse markInvoiceAsPaid(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Facture not found!!"));

        if (invoice.getStatusInvoice() == StatusInvoice.PAID) {
            throw new IllegalStateException("La facture est déjà payée.");
        }

        invoice.setStatusInvoice(StatusInvoice.PAID);
        invoice.setAmountPaid(invoice.getAmount());
        invoice.setPaidInstallments(invoice.getNumberOfInstallments());

        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(updatedInvoice);
    }

    @SneakyThrows
    public List<InvoiceResponse> getInvoicesByStudentId(Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException(" User not authorized!!");
        }
        String currentUserEmail = authentication.getName();

        if (currentUserEmail == null) {
            throw new IllegalArgumentException("Email user not found!!");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Long currentUserId;
        if (!isAdmin) {
            currentUserId = studentRepo.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with email:" + currentUserEmail))
                    .getId();

            if (!currentUserId.equals(studentId)) {
                throw new AccessDeniedException("Access not authorized!!");
            }
        }

        List<Invoice> invoices = invoiceRepo.findByStudentId(studentId);
        return invoices.stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }


    //verifie que la facture existes et que ttes les tranches ne sont pas payees
    @Transactional
    public void requestPayment(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found!!"));

        if (invoice.getPaidInstallments() >= invoice.getNumberOfInstallments()) {
            throw new IllegalStateException("All installments have already been paid.");
        }
    }

    //pour le paiement en tranches
    @Transactional
    public InvoiceResponse markInstallmentPaid(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found!!"));

        if (invoice.getPaidInstallments() >= invoice.getNumberOfInstallments()) {
            throw new IllegalStateException("All installments have already been paid.");
        }

        invoice.setAmountPaid(invoice.getAmountPaid().add(invoice.getInstallmentAmount()));
        invoice.setPaidInstallments(invoice.getPaidInstallments() + 1);

        if (invoice.getPaidInstallments() == invoice.getNumberOfInstallments()) {
            invoice.setStatusInvoice(StatusInvoice.PAID);
        } else {
            invoice.setStatusInvoice(StatusInvoice.PARTIALLY_PAID);
        }

        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(updatedInvoice);
    }


}
