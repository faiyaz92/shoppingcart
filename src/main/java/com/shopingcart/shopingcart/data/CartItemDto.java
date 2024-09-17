package com.shopingcart.shopingcart.data;


import lombok.Data;

@Data
public class CartItemDto {
private Long id;
private CartDto cart;
private ProductDto product;
private Integer quantity;
}