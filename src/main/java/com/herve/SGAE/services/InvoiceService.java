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
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final InvoiceMapper invoiceMapper;
    private final StudentRepo studentRepo;
    private final InvoiceService invoiceService;
    private final StudentSecurityService studentSecurityService;

    public InvoiceService(InvoiceRepo invoiceRepo, InvoiceMapper invoiceMapper, StudentRepo studentRepo, @Lazy InvoiceService invoiceService, StudentSecurityService studentSecurityService) {
        this.invoiceRepo = invoiceRepo;
        this.invoiceMapper = invoiceMapper;
        this.studentRepo = studentRepo;
        this.invoiceService = invoiceService;
        this.studentSecurityService = studentSecurityService;
    }

    @Transactional
    public InvoiceResponse generateInvoice(InvoiceRequest invoiceRequest) {
        Student student = studentRepo.findById(invoiceRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, student);
        Invoice savedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toResponse(savedInvoice);
    }

    /**
      Processus de génération de la facture pour la catégorie de permis :
      1. Après l'inscription, l'administrateur génère une facture pour la catégorie de permis choisie.
      2. Le montant total dépend de la catégorie (ex. : 150 000 FCFA pour la catégorie B).
      3. La facture est divisée en 4 tranches égales (ex. : 25 000 FCFA par tranche).
      4. La réponse inclut les détails de la facture avec les tranches.
     */

    @Transactional
    public InvoiceResponse generatePermitInvoice(PermitInvoiceRequest permitInvoiceRequest) {
        Student student = studentRepo.findById(permitInvoiceRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found!"));

        BigDecimal permitPrice = new BigDecimal(student.getPermitCategory().getPrice());

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setAmount(permitPrice);
        invoiceRequest.setDateDue(LocalDate.now().plusMonths(4));
        invoiceRequest.setStudentId(student.getId());
        invoiceRequest.setNumberOfInstallments(4);
        invoiceRequest.setPermitCategory(student.getPermitCategory());

        return invoiceService.generateInvoice(invoiceRequest);
    }


    /*
     * Processus de paiement d'une tranche de facture :
     * 1. L'étudiant paie une tranche (ex. : 25 000 FCFA) à l'auto-école.
     * 2. L'administrateur ou le moniteur marque la tranche comme payée dans le système.
     * 3. Le système met à jour le montant payé et le nombre de tranches payées.
     * 4. Si toutes les tranches sont payées, le statut de la facture passe à "PAID".
     */
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

    //permet de recuperer les factures d'un student
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoiceByStudentId(Long studentId) throws AccessDeniedException {

        if (!studentSecurityService.isAuthorized(studentId)){
            throw  new AccessDeniedException("access not authorized!!");
        }
        List<Invoice> invoices = invoiceRepo.findByStudentId(studentId);
        return invoices.stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }

}
