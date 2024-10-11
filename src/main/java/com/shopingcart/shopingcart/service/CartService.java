package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.responses.CartResponseDto;

public interface CartService {
    CartResponseDto createCart(Long userId);
    CartResponseDto addProductToCart(Long cartId, Long productId, int quantity); // Change return type to CartResponseDto
    CartResponseDto getCartItems(Long cartId);
}
