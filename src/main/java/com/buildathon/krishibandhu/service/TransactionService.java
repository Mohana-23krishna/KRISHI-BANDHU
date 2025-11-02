package com.buildathon.krishibandhu.service;

import com.buildathon.krishibandhu.model.Crop;
import com.buildathon.krishibandhu.model.Farmer;
import com.buildathon.krishibandhu.model.Transaction;
import com.buildathon.krishibandhu.repository.CropRepository;
import com.buildathon.krishibandhu.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CropRepository cropRepository;
    
    @Autowired
    private FarmerService farmerService;
    
    public Transaction createTransaction(Long cropId, Long buyerId, Double quantity, com.buildathon.krishibandhu.model.Buyer buyerObj) {
        Crop crop = cropRepository.findById(cropId).orElse(null);
        if (crop == null || crop.getAvailableQty() < quantity) {
            return null;
        }
        
        Transaction transaction = new Transaction();
        transaction.setCrop(crop);
        transaction.setBuyer(buyerObj);
        transaction.setQuantity(quantity);
        transaction.setTotalAmount(quantity * crop.getPrice());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus("COMPLETED");
        
        // Update crop availability
        crop.setAvailableQty(crop.getAvailableQty() - quantity);
        cropRepository.save(crop);
        
        // Update farmer credit score
        Farmer farmer = crop.getFarmer();
        farmer.setCreditScore(farmer.getCreditScore() + 10.0); // Add 10 points per transaction
        farmerService.save(farmer);
        
        return transactionRepository.save(transaction);
    }
}

