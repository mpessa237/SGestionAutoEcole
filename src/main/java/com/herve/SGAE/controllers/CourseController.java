package com.herve.SGAE.controllers;

import com.herve.SGAE.dtos.CourseRequest;
import com.herve.SGAE.dtos.CourseResponse;
import com.herve.SGAE.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public ResponseEntity<CourseResponse> save(@RequestBody CourseRequest courseRequest){
        CourseResponse response = courseService.scheduleCourse(courseRequest);
        return ResponseEntity.ok(response);
    }
}
