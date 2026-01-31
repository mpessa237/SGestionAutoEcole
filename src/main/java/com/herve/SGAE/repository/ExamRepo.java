package com.herve.SGAE.repository;

import com.herve.SGAE.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepo extends JpaRepository<Exam,Long> {
}
