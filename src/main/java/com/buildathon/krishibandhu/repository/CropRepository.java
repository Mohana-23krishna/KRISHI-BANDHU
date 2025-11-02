package com.buildathon.krishibandhu.repository;

import com.buildathon.krishibandhu.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByFarmerId(Long farmerId);
    List<Crop> findByAvailableQtyGreaterThan(Double qty);
}

