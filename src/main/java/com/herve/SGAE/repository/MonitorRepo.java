package com.herve.SGAE.repository;

import com.herve.SGAE.models.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepo extends JpaRepository<Monitor,Long> {
}
