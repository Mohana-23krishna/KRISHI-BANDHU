package com.buildathon.krishibandhu.repository;

import com.buildathon.krishibandhu.model.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, Long> {
    List<Transporter> findByAvailable(Boolean available);
}




