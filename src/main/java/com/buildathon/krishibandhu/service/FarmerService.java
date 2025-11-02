package com.buildathon.krishibandhu.service;

import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmerService {
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    public Farmer registerOrGetFarmer(String phone, String language) {
        Optional<Farmer> existing = farmerRepository.findByPhone(phone);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        Farmer farmer = new Farmer();
        farmer.setPhone(phone);
        farmer.setLanguage(language);
        farmer.setCreditScore(0.0);
        farmer.setName("Farmer " + phone.substring(phone.length() - 4));
        return farmerRepository.save(farmer);
    }
    
    public Farmer getFarmerByPhone(String phone) {
        return farmerRepository.findByPhone(phone).orElse(null);
    }
    
    public Farmer save(Farmer farmer) {
        return farmerRepository.save(farmer);
    }
}

