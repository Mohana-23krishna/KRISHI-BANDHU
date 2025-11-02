package com.buildathon.krishibandhu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "crop_id")
    private Crop crop;
    
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;
    
    private Double quantity;
    
    private Double totalAmount;
    
    private LocalDateTime transactionDate;
    
    private String status = "COMPLETED";
}




