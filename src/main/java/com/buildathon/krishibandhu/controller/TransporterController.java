package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Transporter;
import com.buildathon.krishibandhu.repository.TransporterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@CrossOrigin(origins = "*")
public class TransporterController {
    
    @Autowired
    private TransporterRepository transporterRepository;
    
    @PostMapping
    public ResponseEntity<Transporter> addTransporter(@RequestBody Transporter transporter) {
        return ResponseEntity.ok(transporterRepository.save(transporter));
    }
    
    @GetMapping
    public ResponseEntity<List<Transporter>> getAll() {
        return ResponseEntity.ok(transporterRepository.findAll());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Transporter>> getAvailable() {
        return ResponseEntity.ok(transporterRepository.findByAvailable(true));
    }
}




