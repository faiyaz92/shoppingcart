package com.shopingcart.shopingcart.controller;

import com.shopingcart.shopingcart.data.CartDto;
import com.shopingcart.shopingcart.data.CartItemDto;
import com.shopingcart.shopingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<CartDto> createCart(@RequestParam Long id) {
        CartDto cartDTO = cartService.createCart(id);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add-product")
    public ResponseEntity<CartItemDto> addProductToCart(@RequestParam Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        CartItemDto cartItemDTO = cartService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartItemDTO);
    }

    @GetMapping("/get-cart-items/{cartId}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long cartId) {
        List<CartItemDto> cartItemDTOS = cartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItemDTOS);
    }
}
