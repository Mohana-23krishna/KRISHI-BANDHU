package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Buyer;
import com.buildathon.krishibandhu.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/buyers")
@CrossOrigin(origins = "*")
public class BuyerController {
    
    @Autowired
    private BuyerService buyerService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");
            String phone = request.getOrDefault("phone", "");
            
            if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Name, email, and password are required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Buyer buyer = buyerService.register(name, email, password, phone);
            return ResponseEntity.ok(buyer);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            if (email == null || password == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email and password are required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Buyer buyer = buyerService.login(email, password);
            if (buyer != null) {
                return ResponseEntity.ok(buyer);
            }
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getById(@PathVariable Long id) {
        Buyer buyer = buyerService.getById(id);
        return buyer != null ? ResponseEntity.ok(buyer) : ResponseEntity.notFound().build();
    }
}




