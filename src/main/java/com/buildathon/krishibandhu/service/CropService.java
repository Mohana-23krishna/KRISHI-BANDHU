package com.buildathon.krishibandhu.service;

import com.buildathon.krishibandhu.model.Crop;
import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CropService {
    
    @Autowired
    private CropRepository cropRepository;
    
    public Crop createCrop(Farmer farmer, String cropName, Double quantity, Double price, String location) {
        Crop crop = new Crop();
        crop.setFarmer(farmer);
        crop.setCropName(cropName);
        crop.setQuantity(quantity);
        crop.setAvailableQty(quantity);
        crop.setPrice(price);
        crop.setLocation(location);
        crop.setHarvestDate(LocalDate.now());
        crop.setQualityGrade("A");
        crop.setIsOrganic(false);
        return cropRepository.save(crop);
    }
}

