package com.buildathon.krishibandhu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;
    
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;
    
    private Integer stars;
    
    private String comment;
    
    private LocalDateTime ratedDate;
}




