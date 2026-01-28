package com.herve.SGAE.models;

import com.herve.SGAE.enums.StatusCourse;
import com.herve.SGAE.enums.TypeCourse;
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
@Table(name = "courses")
@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeCourse typeCourse;

    private LocalDateTime dateHour;
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private StatusCourse statusCourse;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
