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



    @Transactional
    public InvoiceResponse generateInitialInvoice(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getPermitCategory() == null) {
            throw new IllegalArgumentException("The student does not have a defined license category");
        }
        BigDecimal amount = new BigDecimal(student.getPermitCategory().getPrice());

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(amount);
        invoiceRequest.setDateDue(LocalDate.now().plusDays(14));
        invoiceRequest.setPermitCategory(student.getPermitCategory());
        invoiceRequest.setStudentId(studentId);
        invoiceRequest.setNumberOfInstallments(4);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        invoice.setAccessStartDate(LocalDate.now());
        invoice.setAccessEndDate(LocalDate.now().plusDays(14));

        BigDecimal installmentAmount = invoice.getAmount().divide(new BigDecimal(invoice.getNumberOfInstallments()), 2, RoundingMode.HALF_UP);
        invoice.setInstallmentAmount(installmentAmount);

        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

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

    //methode pour consulter la liste des factures (seul le student et admin)
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



    //methode qui vas permettre vas permettre de generer une facture en tranches
    @Transactional
    public InvoiceResponse generateInstallmentInvoice(Long studentId, int numberOfInstallments) {
        if (numberOfInstallments > 4) {
            throw new IllegalArgumentException("The number of slices cannot exceed 4.");
        }

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found!!"));

        if (student.getPermitCategory() == null) {
            throw new IllegalArgumentException("The student does not have a defined permit category");
        }

        BigDecimal totalAmount = new BigDecimal(student.getPermitCategory().getPrice());
        BigDecimal installmentAmount = totalAmount.divide(new BigDecimal(numberOfInstallments), 2, RoundingMode.HALF_UP);

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(totalAmount);
        invoiceRequest.setDateDue(LocalDate.now().plusMonths(numberOfInstallments));
        invoiceRequest.setPermitCategory(student.getPermitCategory());
        invoiceRequest.setStudentId(studentId);
        invoiceRequest.setNumberOfInstallments(numberOfInstallments);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        invoice.setInstallmentAmount(installmentAmount);
        invoice.setStatusInvoice(StatusInvoice.PENDING);

        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

    //methode pour le paiement en tranche
    @SneakyThrows
    @Transactional
    public InvoiceResponse payInstallment(Long invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found!!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Student currentStudent = studentRepo.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Student not found!!"));

        if (!invoice.getStudent().getId().equals(currentStudent.getId())) {
            throw new AccessDeniedException("This invoice does not belong to you.");
        }

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

        Payment payment = new Payment();
        payment.setAmount(invoice.getInstallmentAmount());
        payment.setDatePayment(LocalDateTime.now());
        payment.setInvoice(invoice);
        invoice.getPaymentList().add(payment);

        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(updatedInvoice);
    }

}
