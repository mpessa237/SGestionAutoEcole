package com.herve.SGAE.models;

import com.herve.SGAE.enums.PermitCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
@Entity
public class Student extends User{

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    private PermitCategory permitCategory;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams  = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

}
