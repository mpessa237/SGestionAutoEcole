package com.herve.SGAE.models;

import com.herve.SGAE.enums.TypeDocument;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documents")
@Entity
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String pathFile;

    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
