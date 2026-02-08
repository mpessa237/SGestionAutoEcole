package com.herve.SGAE.services;

import com.herve.SGAE.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("studentSecurity")
@RequiredArgsConstructor
public class StudentSecurityService {

    private StudentRepo studentRepo;

    
    public boolean isAuthorized(Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String currentUserEmail = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        Long currentUserId = getCurrentUserId(currentUserEmail);
        return currentUserId.equals(studentId);
    }
    
    private Long getCurrentUserId(String email) {
        return studentRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("student not found!!"))
                .getId();
    }


}
