package com.herve.SGAE.models;

import com.herve.SGAE.enums.PermitCategory;
import com.herve.SGAE.enums.StatusInvoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEmission;
    private LocalDate dateDue;
    private BigDecimal amount;
    private BigDecimal amountPaid = BigDecimal.ZERO;
    private int numberOfInstallments = 0; // Nombre de tranches
    private BigDecimal installmentAmount; // Montant de chaque tranche
    private int paidInstallments = 0; // Nombre de tranches déjà payées

    @Enumerated(EnumType.STRING)
    private StatusInvoice statusInvoice;
    @Enumerated(EnumType.STRING)
    private PermitCategory permitCategory;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student ;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();
}
