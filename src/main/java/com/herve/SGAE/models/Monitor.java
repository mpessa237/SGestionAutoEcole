package com.herve.SGAE.models;

import com.herve.SGAE.enums.StatusMonitor;
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
@Table(name = "monitors")
@Entity
public class Monitor extends User{

    private String phoneNumber;
    private String speciality;

    @Enumerated(EnumType.STRING)
    private StatusMonitor statusMonitor;

    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();
}
