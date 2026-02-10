package com.herve.SGAE.controllers;

import com.herve.SGAE.common.MessageResponse;
import com.herve.SGAE.dtos.*;
import com.herve.SGAE.models.Monitor;
import com.herve.SGAE.services.LoginService;
import com.herve.SGAE.services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegisterController {

    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentRequest studentRequest){

        registerService.registerStudent(studentRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("student register successfully!!"));

    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequest userRequest){
        registerService.registerAdmin(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("admin register successfully!"));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/monitor")
    public ResponseEntity<?> registerMonitor(@RequestBody MonitorRequest monitorRequest){

        registerService.registerMonitor(monitorRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Monitor register successfully!!"));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(loginService.login(loginRequest));
    }

}
