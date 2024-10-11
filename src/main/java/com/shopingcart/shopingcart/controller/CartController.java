package com.shopingcart.shopingcart.controller;

import com.shopingcart.shopingcart.responses.CartResponseDto;
import com.shopingcart.shopingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<CartResponseDto> createCart(@RequestParam Long userId) {
        CartResponseDto cartResponseDto = cartService.createCart(userId);
        return ResponseEntity.ok(cartResponseDto);
    }

    @PostMapping("/add-product")
    public ResponseEntity<CartResponseDto> addProductToCart(
            @RequestParam Long cartId, 
            @RequestParam Long productId, 
            @RequestParam int quantity) {
        CartResponseDto cartResponseDto = cartService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartResponseDto);
    }

    @GetMapping("/get-cart-items/{cartId}")
    public ResponseEntity<CartResponseDto> getCartItems(@PathVariable Long cartId) {
        CartResponseDto cartResponseDto = cartService.getCartItems(cartId);
        return ResponseEntity.ok(cartResponseDto);
    }
}
