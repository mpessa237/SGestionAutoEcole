package com.herve.SGAE.mappers;

import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.enums.StatusCourse;
import com.herve.SGAE.enums.TypeCourse;
import com.herve.SGAE.models.*;
import com.herve.SGAE.repository.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final VehicleRepo vehicleRepo;

    public Course toEntity(CourseRequest courseRequest, Monitor monitor) {
        Course course = new Course();
        course.setTypeCourse(courseRequest.getTypeCourse());
        course.setDuration(courseRequest.getDuration());
        course.setDateHour(courseRequest.getDateHour());
        course.setStatusCourse(StatusCourse.PLANNED);
        course.setMonitor(monitor);

        // Si c'est un cours pratique, créer un PracticalCourse
        if (courseRequest.getTypeCourse() == TypeCourse.PRACTICE) {
            PracticalCourse practicalCourse = new PracticalCourse();
            practicalCourse.setCourse(course);
            practicalCourse.setVehicle(vehicleRepo.findById(courseRequest.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle not found")));
            practicalCourse.setStartTimeDate(courseRequest.getDateHour());
            practicalCourse.setEndTimeDate(courseRequest.getDateHour().plusMinutes(courseRequest.getDuration()));
            course.setPracticalCourse(practicalCourse);
        }
        // Si c'est un cours théorique, créer un TheoryCourse
        else if (courseRequest.getTypeCourse() == TypeCourse.THEORY) {
            TheoryCourse theoryCourse = new TheoryCourse();
            theoryCourse.setCourse(course);
            theoryCourse.setStartTimeDate(courseRequest.getDateHour());
            theoryCourse.setEndTimeDate(courseRequest.getDateHour().plusMinutes(courseRequest.getDuration()));
            theoryCourse.setClassroom(courseRequest.getClassroom());
            course.setTheoryCourse(theoryCourse);
        }

        return course;
    }


    public CourseResponse toResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setTypeCourse(course.getTypeCourse());
        courseResponse.setDateHour(course.getDateHour());
        courseResponse.setDuration(course.getDuration());
        courseResponse.setStatusCourse(course.getStatusCourse());
        courseResponse.setMonitorId(course.getMonitor().getId());
        courseResponse.setMonitorFirstname(course.getMonitor().getFirstname());

        // Récupérer les étudiants inscrits
        Set<Student> students = course.getStudents();
        if (students != null) {
            List<Long> studentIds = new ArrayList<>();
            List<String> studentFirstnames = new ArrayList<>();
            for (Student student : students) {
                studentIds.add(student.getId());
                studentFirstnames.add(student.getFirstname());
            }
            courseResponse.setStudentIds(studentIds);
            courseResponse.setStudentFirstnames(studentFirstnames);
        }

        if (course.getTypeCourse() == TypeCourse.PRACTICE && course.getPracticalCourse() != null) {
            courseResponse.setVehicleId(course.getPracticalCourse().getVehicle().getId());
        }
        else if (course.getTypeCourse() == TypeCourse.THEORY && course.getTheoryCourse() != null) {
            courseResponse.setClassroom(course.getTheoryCourse().getClassroom());
        }

        return courseResponse;
    }

}
