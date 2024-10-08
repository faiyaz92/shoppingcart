package com.shopingcart.shopingcart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Double price;
    private Double initialPrice;
    private String description;
    private String packagingQuantity;
    private String manufacturerName;
    private String distributorName;
    private String images; 
}
