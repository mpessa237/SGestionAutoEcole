package com.herve.SGAE.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePayment;
    private BigDecimal amount;

    //@Enumerated(EnumType.STRING)
    //private ModePaiement modePaiement;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice ;
}
