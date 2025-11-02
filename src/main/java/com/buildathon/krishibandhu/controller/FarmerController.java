package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.repository.FarmerRepository;
import com.buildathon.krishibandhu.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/farmers")
@CrossOrigin(origins = "*")
public class FarmerController {
    
    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private FarmerService farmerService;
    
    @PostMapping
    public ResponseEntity<Farmer> register(@RequestBody Farmer farmer) {
        Farmer saved = farmerService.registerOrGetFarmer(farmer.getPhone(), farmer.getLanguage());
        if (farmer.getName() != null) saved.setName(farmer.getName());
        if (farmer.getAddress() != null) saved.setAddress(farmer.getAddress());
        if (farmer.getState() != null) saved.setState(farmer.getState());
        saved = farmerService.save(saved);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping
    public ResponseEntity<List<Farmer>> getAll() {
        return ResponseEntity.ok(farmerRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getById(@PathVariable Long id) {
        Optional<Farmer> farmer = farmerRepository.findById(id);
        return farmer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Farmer> getByPhone(@PathVariable String phone) {
        Optional<Farmer> farmer = farmerRepository.findByPhone(phone);
        return farmer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}




