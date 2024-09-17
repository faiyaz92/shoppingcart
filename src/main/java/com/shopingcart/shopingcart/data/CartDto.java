package com.shopingcart.shopingcart.data;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
private Long id;
private UserDto user;
private List<CartItemDto> cartItems;
}