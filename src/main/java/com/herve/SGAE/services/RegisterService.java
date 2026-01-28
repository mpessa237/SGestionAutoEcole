package com.herve.SGAE.services;

import com.herve.SGAE.dtos.MonitorRequest;
import com.herve.SGAE.dtos.StudentRequest;
import com.herve.SGAE.dtos.UserRequest;
import com.herve.SGAE.enums.Role;
import com.herve.SGAE.enums.StatusMonitor;
import com.herve.SGAE.enums.StatusUser;
import com.herve.SGAE.exceptions.EmailAlreadyExistsException;
import com.herve.SGAE.models.Monitor;
import com.herve.SGAE.models.Student;
import com.herve.SGAE.models.User;
import com.herve.SGAE.repository.MonitorRepo;
import com.herve.SGAE.repository.StudentRepo;
import com.herve.SGAE.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepo userRepo;
    private final StudentRepo studentRepo;
    private final MonitorRepo monitorRepo;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void registerStudent(StudentRequest studentRequest){

        if (userRepo.findByEmail(studentRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("email " + studentRequest.getEmail() + " est déjà utilisé !");
        }


        Student student = new Student();
        student.setFirstname(studentRequest.getFirstname());
        student.setLastname(studentRequest.getLastname());
        student.setEmail(studentRequest.getEmail());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setDateOfBirth(studentRequest.getDateOfBirth());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setAddress(studentRequest.getAddress());
        student.setRole(Set.of(Role.STUDENT));
        student.setStatusUser(StatusUser.ACTIVATE);

        studentRepo.save(student);
    }

    public void registerMonitor(MonitorRequest monitorRequest) {

        if (userRepo.findByEmail(monitorRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("email" + monitorRequest.getEmail() + "est déjà utilisé !");
        }

        Monitor monitor = new Monitor();
        monitor.setFirstname(monitorRequest.getFirstname());
        monitor.setLastname(monitorRequest.getLastname());
        monitor.setEmail(monitorRequest.getEmail());
        monitor.setPassword(passwordEncoder.encode(monitorRequest.getPassword()));
        monitor.setSpeciality(monitorRequest.getSpeciality());
        monitor.setPhoneNumber(monitorRequest.getPhoneNumber());
        monitor.setStatusMonitor(StatusMonitor.AVAILABLE);
        monitor.setRole(Set.of(Role.MONITOR));

        monitorRepo.save(monitor);
    }


    public void registerAdmin(UserRequest userRequest) {

        if (userRepo.findByEmail(userRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("email" + userRequest.getEmail() + "already exists!");
        }

        User user = new User();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Set.of(Role.ADMIN));

        userRepo.save(user);
    }


}
