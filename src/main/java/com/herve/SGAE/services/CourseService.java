package com.herve.SGAE.services;

import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.mappers.CourseMapper;
import com.herve.SGAE.models.Course;
import com.herve.SGAE.models.Monitor;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.repository.CourseRepo;
import com.herve.SGAE.repository.MonitorRepo;
import com.herve.SGAE.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final StudentRepo studentRepo;
    private final MonitorRepo monitorRepo;

    @Transactional
    public CourseResponse scheduleCourse(CourseRequest courseRequest) {
        Student student = studentRepo.findById(courseRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Monitor monitor = monitorRepo.findById(courseRequest.getMonitorId())
                .orElseThrow(() -> new IllegalArgumentException("Monitor not found"));

        Course course = courseMapper.toEntity(courseRequest, student, monitor);
        Course savedCourse = courseRepo.save(course);

        return courseMapper.toResponse(savedCourse);
    }

}
