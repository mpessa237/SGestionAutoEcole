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
@Table(name = "practical_Exams")
@Entity
public class PracticalExam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle ;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(nullable = false)
    private LocalDateTime startTimeDate ;

    @Column(nullable = false)
    private LocalDateTime endTimeDate;
}
