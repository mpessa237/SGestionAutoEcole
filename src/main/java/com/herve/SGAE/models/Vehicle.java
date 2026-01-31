package com.herve.SGAE.models;

import com.herve.SGAE.enums.StatusVehicle;
import com.herve.SGAE.enums.TypeVehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
@Entity
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String registration; //immatriculation

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private StatusVehicle statusVehicle;

    @Enumerated(EnumType.STRING)
    private TypeVehicle typeVehicle;

    private String color;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PracticalCourse> practicalCourses  = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PracticalExam> practicalExams  = new ArrayList<>();
}
