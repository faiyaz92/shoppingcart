package com.shopingcart.shopingcart.responses;

import com.shopingcart.shopingcart.data.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class CartResponseDto {
    private Long id;
    private UserDto user;
    private List<ProductResponseDto> products; // List of products with quantities
    private Double finalAmount; // Total amount of the cart
    private Double totalSaved; // Total saved amount
}
