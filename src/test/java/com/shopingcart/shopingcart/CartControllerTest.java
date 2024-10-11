package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.controller.CartController;
import com.shopingcart.shopingcart.responses.CartResponseDto;
import com.shopingcart.shopingcart.service.CartService;
import com.shopingcart.shopingcart.globalerror.CartAlreadyExistsException;
import com.shopingcart.shopingcart.globalerror.CartNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCart_Success() {
        // Arrange
        Long userId = 1L;
        CartResponseDto expectedCartResponse = new CartResponseDto();
        when(cartService.createCart(userId)).thenReturn(expectedCartResponse);

        // Act
        ResponseEntity<CartResponseDto> response = cartController.createCart(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCartResponse, response.getBody());
    }

    @Test
    public void testCreateCart_CartAlreadyExists() {
        // Arrange
        Long userId = 1L;
        when(cartService.createCart(userId)).thenThrow(new CartAlreadyExistsException("Cart already exists"));

        // Act
        Exception exception = assertThrows(CartAlreadyExistsException.class, () -> {
            cartController.createCart(userId);
        });

        // Assert
        assertEquals("Cart already exists", exception.getMessage());
    }

    @Test
    public void testAddProductToCart_Success() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;
        CartResponseDto expectedCartResponse = new CartResponseDto();
        when(cartService.addProductToCart(cartId, productId, quantity)).thenReturn(expectedCartResponse);

        // Act
        ResponseEntity<CartResponseDto> response = cartController.addProductToCart(cartId, productId, quantity);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCartResponse, response.getBody());
    }

    @Test
    public void testAddProductToCart_CartNotFound() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;
        when(cartService.addProductToCart(cartId, productId, quantity)).thenThrow(new CartNotFoundException("Cart not found"));

        // Act
        Exception exception = assertThrows(CartNotFoundException.class, () -> {
            cartController.addProductToCart(cartId, productId, quantity);
        });

        // Assert
        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    public void testGetCartItems_Success() {
        // Arrange
        Long cartId = 1L;
        CartResponseDto expectedCartResponse = new CartResponseDto();
        when(cartService.getCartItems(cartId)).thenReturn(expectedCartResponse);

        // Act
        ResponseEntity<CartResponseDto> response = cartController.getCartItems(cartId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCartResponse, response.getBody());
    }

    @Test
    public void testGetCartItems_CartNotFound() {
        // Arrange
        Long cartId = 1L;
        when(cartService.getCartItems(cartId)).thenThrow(new CartNotFoundException("Cart not found"));

        // Act
        Exception exception = assertThrows(CartNotFoundException.class, () -> {
            cartController.getCartItems(cartId);
        });

        // Assert
        assertEquals("Cart not found", exception.getMessage());
    }
}
