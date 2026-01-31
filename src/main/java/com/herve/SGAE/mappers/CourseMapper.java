package com.herve.SGAE.mappers;

import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.enums.StatusCourse;
import com.herve.SGAE.models.Course;
import com.herve.SGAE.models.Monitor;
import com.herve.SGAE.models.Student;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequest courseRequest, Student student, Monitor monitor){

        Course course = new Course();
        course.setTypeCourse(courseRequest.getTypeCourse());
        course.setDuration(courseRequest.getDuration());
        course.setDateHour(courseRequest.getDateHour());
        course.setStatusCourse(StatusCourse.PLANNED);
        course.setStudent(student);
        course.setMonitor(monitor);

        return course;
    }

    public CourseResponse toResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setTypeCourse(course.getTypeCourse());
        courseResponse.setDateHour(course.getDateHour());
        courseResponse.setDuration(course.getDuration());
        courseResponse.setStatusCourse(course.getStatusCourse());
        courseResponse.setStudentId(course.getStudent().getId());
        courseResponse.setStudentFirstname(course.getStudent().getFirstname());
        courseResponse.setMonitorId(course.getMonitor().getId());
        courseResponse.setMonitorFirstname(course.getMonitor().getFirstname());

        return courseResponse;
    }
}
