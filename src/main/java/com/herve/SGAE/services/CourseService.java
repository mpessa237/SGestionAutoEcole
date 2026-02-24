package com.herve.SGAE.services;


import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.mappers.CourseMapper;
import com.herve.SGAE.models.Course;
import com.herve.SGAE.models.Monitor;
import com.herve.SGAE.models.PracticalCourse;
import com.herve.SGAE.models.Vehicle;
import com.herve.SGAE.repository.CourseRepo;
import com.herve.SGAE.repository.MonitorRepo;
import com.herve.SGAE.repository.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepo courseRepo;
    private final VehicleRepo vehicleRepo;
    private final MonitorRepo monitorRepo;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseResponse createPracticalCourse(CourseRequest courseRequest) {

        Monitor monitor = monitorRepo.findById(courseRequest.getMonitorId())
                .orElseThrow(() -> new IllegalArgumentException("Monitor not found!!"));

        Vehicle vehicle = vehicleRepo.findById(courseRequest.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Véhicle not found!!"));

        Course course = courseMapper.toEntity(courseRequest, monitor);

        PracticalCourse practicalCourse = new PracticalCourse();
        practicalCourse.setCourse(course);
        practicalCourse.setVehicle(vehicle);
        practicalCourse.setStartTimeDate(courseRequest.getDateHour());
        practicalCourse.setEndTimeDate(courseRequest.getDateHour().plusMinutes(courseRequest.getDuration()));

        course.setPracticalCourse(practicalCourse);

        Course savedCourse = courseRepo.save(course);

        return courseMapper.toResponse(savedCourse);
    }



}
