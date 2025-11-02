package com.buildathon.krishibandhu.controller;

import com.buildathon.krishibandhu.model.Transaction;
import com.buildathon.krishibandhu.repository.TransactionRepository;
import com.buildathon.krishibandhu.service.BuyerService;
import com.buildathon.krishibandhu.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BuyerService buyerService;
    
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestParam Long cropId,
            @RequestParam Long buyerId,
            @RequestParam Double quantity) {
        com.buildathon.krishibandhu.model.Buyer buyer = buyerService.getById(buyerId);
        if (buyer == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Buyer not found");
            return ResponseEntity.badRequest().body(error);
        }
        
        Transaction transaction = transactionService.createTransaction(cropId, buyerId, quantity, buyer);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Transaction failed - insufficient quantity or crop not found");
        return ResponseEntity.badRequest().body(error);
    }
    
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(transactionRepository.findByBuyerId(buyerId));
    }
    
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Transaction>> getByFarmer(@PathVariable Long farmerId) {
        return ResponseEntity.ok(transactionRepository.findByCropFarmerId(farmerId));
    }
}




