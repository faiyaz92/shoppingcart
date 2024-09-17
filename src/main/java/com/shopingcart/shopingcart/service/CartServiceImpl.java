package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.CartDto;
import com.shopingcart.shopingcart.data.CartItemDto;
import com.shopingcart.shopingcart.entity.CartEntity;
import com.shopingcart.shopingcart.entity.CartItemEntity;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.entity.UserEntity;
import com.shopingcart.shopingcart.repository.CartItemRepository;
import com.shopingcart.shopingcart.repository.CartRepository;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartDto createCart(Long id) {
        CartEntity cart = new CartEntity();
        UserEntity userEntity = userRepository.findById(id).get();
        cart.setUser(userEntity);
        CartEntity savedCart = cartRepository.save(cart);
        CartDto cartDTO = new CartDto();
        BeanUtils.copyProperties(savedCart, cartDTO);
        return cartDTO;
    }

    @Override
    public CartItemDto addProductToCart(Long cartId, Long productId, int quantity) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow();
        ProductEntity product = productRepository.findById(productId).orElseThrow();

        CartItemEntity existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            CartItemEntity savedCartItem = cartItemRepository.save(existingCartItem);
            CartItemDto cartItemDTO = new CartItemDto();
            BeanUtils.copyProperties(savedCartItem, cartItemDTO);
            return cartItemDTO;
        } else {
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            CartItemEntity savedCartItem = cartItemRepository.save(cartItem);
            CartItemDto cartItemDTO = new CartItemDto();
            BeanUtils.copyProperties(savedCartItem, cartItemDTO);
            return cartItemDTO;
        }
    }

    @Override
    public List<CartItemDto> getCartItems(Long cartId) {
        List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cartId);
        List<CartItemDto> cartItemDTOS = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            CartItemDto cartItemDTO = new CartItemDto();
            BeanUtils.copyProperties(cartItem, cartItemDTO);
            cartItemDTOS.add(cartItemDTO);
        }
        return cartItemDTOS;
    }
}
