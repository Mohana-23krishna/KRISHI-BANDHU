package com.buildathon.krishibandhu.service;

import com.buildathon.krishibandhu.model.Buyer;
import com.buildathon.krishibandhu.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerService {
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    public Buyer register(String name, String email, String password, String phone) {
        // Check if buyer already exists
        Optional<Buyer> existing = buyerRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new RuntimeException("A buyer with this email already exists. Please login instead.");
        }
        
        Buyer buyer = new Buyer();
        buyer.setName(name);
        buyer.setEmail(email);
        buyer.setPassword(password); // In production, hash this!
        buyer.setPhone(phone);
        return buyerRepository.save(buyer);
    }
    
    public Buyer login(String email, String password) {
        Optional<Buyer> buyer = buyerRepository.findByEmail(email);
        if (buyer.isPresent() && buyer.get().getPassword().equals(password)) {
            return buyer.get();
        }
        return null;
    }
    
    public Buyer getById(Long id) {
        return buyerRepository.findById(id).orElse(null);
    }
}




