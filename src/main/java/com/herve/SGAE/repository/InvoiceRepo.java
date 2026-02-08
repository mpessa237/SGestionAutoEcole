package com.herve.SGAE.repository;

import com.herve.SGAE.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice,Long> {

    List<Invoice> findByStudentId(Long studentId);
}
