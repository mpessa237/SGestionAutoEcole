package com.herve.SGAE.mappers;

import com.herve.SGAE.dtos.StudentRequest;
import com.herve.SGAE.dtos.StudentResponse;
import com.herve.SGAE.models.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest studentRequest) {
        Student student = new Student();
        student.setFirstname(studentRequest.getFirstname());
        student.setLastname(studentRequest.getLastname());
        student.setEmail(studentRequest.getEmail());
        student.setPassword(studentRequest.getPassword());
        student.setDateOfBirth(studentRequest.getDateOfBirth());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setAddress(studentRequest.getAddress());
        return student;
    }

    public StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstname(student.getFirstname());
        response.setLastname(student.getLastname());
        response.setEmail(student.getEmail());
        response.setDateOfBirth(student.getDateOfBirth());
        response.setPhoneNumber(student.getPhoneNumber());
        response.setAddress(student.getAddress());

        return response;
    }
}
