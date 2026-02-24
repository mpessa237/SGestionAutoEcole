package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/practical")
    public ResponseEntity<CourseResponse> createPracticalCourse(@RequestBody CourseRequest courseRequest) {
        CourseResponse response = courseService.createPracticalCourse(courseRequest);
        return ResponseEntity.ok(response);
    }


}
