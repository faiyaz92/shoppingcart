package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.CartDto;
import com.shopingcart.shopingcart.data.CartItemDto;

import java.util.List;

public interface CartService {
    CartDto createCart(Long userId);
    CartItemDto addProductToCart(Long cartId, Long productId, int quantity);
    List<CartItemDto> getCartItems(Long cartId);
}
