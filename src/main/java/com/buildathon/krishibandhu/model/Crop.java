package com.buildathon.krishibandhu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "crops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;
    
    private String cropName;
    
    private Double quantity;
    
    private Double price;
    
    private Double availableQty;
    
    private LocalDate harvestDate;
    
    private String location;
    
    private String qualityGrade;
    
    private Boolean isOrganic = false;
}

