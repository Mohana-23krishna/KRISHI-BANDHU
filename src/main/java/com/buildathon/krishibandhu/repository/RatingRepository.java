package com.buildathon.krishibandhu.repository;

import com.buildathon.krishibandhu.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByFarmerId(Long farmerId);
}




