package com.buildathon.krishibandhu.config;

import com.buildathon.krishibandhu.model.Buyer;
import com.buildathon.krishibandhu.model.Crop;
import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.repository.BuyerRepository;
import com.buildathon.krishibandhu.repository.CropRepository;
import com.buildathon.krishibandhu.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private CropRepository cropRepository;
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (cropRepository.count() > 0) {
            return; // Data already initialized
        }
        
        // Create sample farmers
        Farmer farmer1 = new Farmer();
        farmer1.setName("Rajesh Kumar");
        farmer1.setPhone("9876543210");
        farmer1.setAddress("Mumbai, Maharashtra");
        farmer1.setState("Maharashtra");
        farmer1.setLanguage("hi");
        farmer1.setCreditScore(85.0);
        farmer1 = farmerRepository.save(farmer1);
        
        Farmer farmer2 = new Farmer();
        farmer2.setName("Priya Reddy");
        farmer2.setPhone("9876543211");
        farmer2.setAddress("Hyderabad, Telangana");
        farmer2.setState("Telangana");
        farmer2.setLanguage("te");
        farmer2.setCreditScore(92.0);
        farmer2 = farmerRepository.save(farmer2);
        
        Farmer farmer3 = new Farmer();
        farmer3.setName("Suresh Kumar");
        farmer3.setPhone("9876543212");
        farmer3.setAddress("Chennai, Tamil Nadu");
        farmer3.setState("Tamil Nadu");
        farmer3.setLanguage("ta");
        farmer3.setCreditScore(78.0);
        farmer3 = farmerRepository.save(farmer3);
        
        // Create sample crops
        Crop crop1 = new Crop();
        crop1.setFarmer(farmer1);
        crop1.setCropName("wheat");
        crop1.setQuantity(500.0);
        crop1.setAvailableQty(450.0);
        crop1.setPrice(25.0);
        crop1.setLocation("Mumbai, Maharashtra");
        crop1.setHarvestDate(LocalDate.now().minusDays(5));
        crop1.setQualityGrade("A");
        crop1.setIsOrganic(true);
        cropRepository.save(crop1);
        
        Crop crop2 = new Crop();
        crop2.setFarmer(farmer2);
        crop2.setCropName("rice");
        crop2.setQuantity(300.0);
        crop2.setAvailableQty(280.0);
        crop2.setPrice(35.0);
        crop2.setLocation("Hyderabad, Telangana");
        crop2.setHarvestDate(LocalDate.now().minusDays(3));
        crop2.setQualityGrade("A");
        crop2.setIsOrganic(false);
        cropRepository.save(crop2);
        
        Crop crop3 = new Crop();
        crop3.setFarmer(farmer3);
        crop3.setCropName("tomato");
        crop3.setQuantity(200.0);
        crop3.setAvailableQty(180.0);
        crop3.setPrice(40.0);
        crop3.setLocation("Chennai, Tamil Nadu");
        crop3.setHarvestDate(LocalDate.now().minusDays(1));
        crop3.setQualityGrade("A+");
        crop3.setIsOrganic(true);
        cropRepository.save(crop3);
        
        Crop crop4 = new Crop();
        crop4.setFarmer(farmer1);
        crop4.setCropName("cotton");
        crop4.setQuantity(400.0);
        crop4.setAvailableQty(400.0);
        crop4.setPrice(55.0);
        crop4.setLocation("Mumbai, Maharashtra");
        crop4.setHarvestDate(LocalDate.now().minusDays(7));
        crop4.setQualityGrade("B");
        crop4.setIsOrganic(false);
        cropRepository.save(crop4);
        
        Crop crop5 = new Crop();
        crop5.setFarmer(farmer2);
        crop5.setCropName("sugarcane");
        crop5.setQuantity(600.0);
        crop5.setAvailableQty(550.0);
        crop5.setPrice(30.0);
        crop5.setLocation("Hyderabad, Telangana");
        crop5.setHarvestDate(LocalDate.now().minusDays(2));
        crop5.setQualityGrade("A");
        crop5.setIsOrganic(false);
        cropRepository.save(crop5);
        
        // Create sample buyer
        Buyer buyer = new Buyer();
        buyer.setName("Sample Buyer");
        buyer.setEmail("buyer@example.com");
        buyer.setPassword("password123");
        buyer.setPhone("9876543299");
        buyerRepository.save(buyer);
        
        System.out.println("✅ Sample data initialized: 3 farmers, 5 crops, 1 buyer");
    }
}

