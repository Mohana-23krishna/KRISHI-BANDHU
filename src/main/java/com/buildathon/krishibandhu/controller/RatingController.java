package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Rating;
import com.buildathon.krishibandhu.repository.RatingRepository;
import com.buildathon.krishibandhu.repository.BuyerRepository;
import com.buildathon.krishibandhu.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody RatingDTO dto) {
        Rating rating = new Rating();
        rating.setBuyer(buyerRepository.findById(dto.getBuyerId()).orElse(null));
        rating.setFarmer(farmerRepository.findById(dto.getFarmerId()).orElse(null));
        rating.setStars(dto.getStars());
        rating.setComment(dto.getComment());
        rating.setRatedDate(LocalDateTime.now());
        return ResponseEntity.ok(ratingRepository.save(rating));
    }
    
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Rating>> getByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(ratingRepository.findByFarmerId(farmerId));
    }
    
    public static class RatingDTO {
        private Long buyerId;
        private Long farmerId;
        private Integer stars;
        private String comment;
        
        public Long getBuyerId() { return buyerId; }
        public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
        public Long getFarmerId() { return farmerId; }
        public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }
        public Integer getStars() { return stars; }
        public void setStars(Integer stars) { this.stars = stars; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}




