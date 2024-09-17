package com.shopingcart.shopingcart.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private Double price;
    private Double initialPrice;
    private String description;
    private String packagingQuantity;
    private String manufacturerName;
    private String distributorName;
    private List<String> images;
}
