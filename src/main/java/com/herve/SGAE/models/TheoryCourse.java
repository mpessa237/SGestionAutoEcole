package com.herve.SGAE.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "theory_Courses")
@Entity
public class TheoryCourse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String classroom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime startTimeDate;

    @Column(nullable = false)
    private LocalDateTime endTimeDate;

}
