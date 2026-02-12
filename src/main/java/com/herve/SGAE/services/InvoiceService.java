package com.herve.SGAE.services;


import com.herve.SGAE.dtos.InvoiceRequest;
import com.herve.SGAE.dtos.InvoiceResponse;
import com.herve.SGAE.enums.StatusInvoice;
import com.herve.SGAE.mappers.InvoiceMapper;
import com.herve.SGAE.models.Invoice;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.models.User;
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
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final StudentRepo studentRepo;
    private final UserRepo userRepo;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    public InvoiceResponse generateInitialInvoice(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getPermitCategory()==null){
            throw new IllegalArgumentException("The student does not have a defined license category");
        }
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(new BigDecimal("10000"));
        invoiceRequest.setDateDue(LocalDate.now().plusDays(14));
        invoiceRequest.setPermitCategory(student.getPermitCategory());
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

    //methode pour consulter la liste des factures (seul le student et admin)
    @SneakyThrows
    public List<InvoiceResponse> getInvoicesByStudentId(Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Utilisateur non authentifié");
        }

        String currentUserEmail = authentication.getName();

        if (currentUserEmail == null) {
            throw new IllegalArgumentException("Email de l'utilisateur non trouvé");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Long currentUserId;
        if (!isAdmin) {
            currentUserId = studentRepo.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé avec l'email: " + currentUserEmail))
                    .getId();

            if (!currentUserId.equals(studentId)) {
                throw new AccessDeniedException("Accès non autorisé");
            }
        }

        List<Invoice> invoices = invoiceRepo.findByStudentId(studentId);
        return invoices.stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }

}
