package com.shopingcart.shopingcart.responses;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDto {
    private Long id;
    private String productName;
    private Double price;
    private Double initialPrice;
    private String description;
    private String packagingQuantity;
    private String manufacturerName;
    private String distributorName;
    private List<String> images;
    private Integer quantity; // Added to keep track of quantities in cart
}
