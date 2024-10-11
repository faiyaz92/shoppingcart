package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.data.UserDto;
import com.shopingcart.shopingcart.entity.CartEntity;
import com.shopingcart.shopingcart.entity.CartItemEntity;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.entity.UserEntity;
import com.shopingcart.shopingcart.globalerror.CartAlreadyExistsException;
import com.shopingcart.shopingcart.globalerror.CartNotFoundException;
import com.shopingcart.shopingcart.repository.CartItemRepository;
import com.shopingcart.shopingcart.repository.CartRepository;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.repository.UserRepository;
import com.shopingcart.shopingcart.responses.CartResponseDto;
import com.shopingcart.shopingcart.responses.ProductResponseDto;
import com.shopingcart.shopingcart.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private UserEntity userEntity;
    private CartEntity cartEntity;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        userEntity = new UserEntity();
        userEntity.setId(1L);
        
        cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setUser(userEntity);
        
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setPrice(100.0);
        productEntity.setInitialPrice(150.0);
    }

    @Test
    void createCart_CartDoesNotExist_CreatesCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(cartRepository.findByUser(userEntity)).thenReturn(null);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        CartResponseDto responseDto = cartService.createCart(1L);
        
        assertNotNull(responseDto);
        assertEquals(cartEntity.getId(), responseDto.getId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    void createCart_CartAlreadyExists_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(cartRepository.findByUser(userEntity)).thenReturn(cartEntity);

        assertThrows(CartAlreadyExistsException.class, () -> {
            cartService.createCart(1L);
        });
    }

    @Test
    void addProductToCart_CartExists_ProductAdded() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(null);
        
        CartResponseDto responseDto = cartService.addProductToCart(1L, 1L, 2);
        
        assertNotNull(responseDto);
        verify(cartItemRepository, times(1)).save(any(CartItemEntity.class));
    }

    @Test
    void addProductToCart_CartNotFound_ThrowsException() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.addProductToCart(1L, 1L, 2);
        });
    }

    @Test
    void addProductToCart_ProductNotFound_ThrowsException() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.addProductToCart(1L, 1L, 2);
        });
    }

    @Test
    void getCartItems_CartExists_ReturnsItems() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartEntity));
        when(cartItemRepository.findAllByCartId(1L)).thenReturn(new ArrayList<>());

        CartResponseDto responseDto = cartService.getCartItems(1L);
        
        assertNotNull(responseDto);
        assertEquals(cartEntity.getId(), responseDto.getId());
    }

    @Test
    void getCartItems_CartNotFound_ThrowsException() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCartItems(1L);
        });
    }
}
