package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Crop;
import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.repository.CropRepository;
import com.buildathon.krishibandhu.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*")
public class CropController {
    
    @Autowired
    private CropRepository cropRepository;
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @PostMapping
    public ResponseEntity<Crop> addCrop(@RequestBody CropDTO dto) {
        Farmer farmer = farmerRepository.findById(dto.getFarmerId()).orElse(null);
        if (farmer == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Crop crop = new Crop();
        crop.setFarmer(farmer);
        crop.setCropName(dto.getCropName());
        crop.setQuantity(dto.getQuantity());
        crop.setPrice(dto.getPrice());
        crop.setAvailableQty(dto.getAvailableQty() != null ? dto.getAvailableQty() : dto.getQuantity());
        crop.setLocation(dto.getLocation());
        crop.setQualityGrade(dto.getQualityGrade());
        crop.setIsOrganic(dto.getIsOrganic() != null ? dto.getIsOrganic() : false);
        crop.setHarvestDate(java.time.LocalDate.now());
        
        return ResponseEntity.ok(cropRepository.save(crop));
    }
    
    @GetMapping
    public ResponseEntity<List<Crop>> getAll() {
        return ResponseEntity.ok(cropRepository.findAll());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Crop>> getAvailable() {
        List<Crop> crops = cropRepository.findByAvailableQtyGreaterThan(0.0);
        return ResponseEntity.ok(crops);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Crop>> search(@RequestParam String cropName) {
        List<Crop> all = cropRepository.findAll();
        List<Crop> filtered = all.stream()
            .filter(c -> c.getCropName().toLowerCase().contains(cropName.toLowerCase()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }
    
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Crop>> getByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(cropRepository.findByFarmerId(farmerId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Crop> getById(@PathVariable Long id) {
        return cropRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // DTO for crop creation
    public static class CropDTO {
        private Long farmerId;
        private String cropName;
        private Double quantity;
        private Double price;
        private Double availableQty;
        private String location;
        private String qualityGrade;
        private Boolean isOrganic;
        
        // Getters and setters
        public Long getFarmerId() { return farmerId; }
        public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }
        public String getCropName() { return cropName; }
        public void setCropName(String cropName) { this.cropName = cropName; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Double getAvailableQty() { return availableQty; }
        public void setAvailableQty(Double availableQty) { this.availableQty = availableQty; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getQualityGrade() { return qualityGrade; }
        public void setQualityGrade(String qualityGrade) { this.qualityGrade = qualityGrade; }
        public Boolean getIsOrganic() { return isOrganic; }
        public void setIsOrganic(Boolean isOrganic) { this.isOrganic = isOrganic; }
    }
}




