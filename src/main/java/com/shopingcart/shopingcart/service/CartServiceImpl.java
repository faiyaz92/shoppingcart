package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.UserDto;
import com.shopingcart.shopingcart.entity.CartEntity;
import com.shopingcart.shopingcart.entity.CartItemEntity;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.entity.UserEntity;
import com.shopingcart.shopingcart.globalerror.CartAlreadyExistsException;
import com.shopingcart.shopingcart.globalerror.CartNotFoundException;
import com.shopingcart.shopingcart.globalerror.UserNotFoundException;
import com.shopingcart.shopingcart.repository.CartItemRepository;
import com.shopingcart.shopingcart.repository.CartRepository;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.repository.UserRepository;
import com.shopingcart.shopingcart.responses.CartResponseDto;
import com.shopingcart.shopingcart.responses.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponseDto createCart(Long userId) {
        try {
            // Fetch the user entity, throw an exception if not found
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Check if a cart already exists for this user
            CartEntity existingCart = cartRepository.findByUser(userEntity);
            if (existingCart != null) {
                throw new CartAlreadyExistsException("Cart already exists for this user");
            }

            // Create a new cart
            CartEntity cart = new CartEntity();
            cart.setUser(userEntity);
            CartEntity savedCart = cartRepository.save(cart);

            // Prepare the response DTO
            CartResponseDto cartResponseDto = new CartResponseDto();
            BeanUtils.copyProperties(savedCart, cartResponseDto);
            return cartResponseDto;

        } catch (UserNotFoundException | CartAlreadyExistsException e) {
            logger.warn("Error creating cart: {}", e.getMessage());
            throw e; // Re-throw known exceptions to be handled by global exception handler
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating cart: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred111");
        }
    }

    @Override
    public CartResponseDto addProductToCart(Long cartId, Long productId, int quantity) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CartNotFoundException("Product not found"));

        CartItemEntity existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return getCartItems(cartId); // Return updated cart response
    }

    @Override
    public CartResponseDto getCartItems(Long cartId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cartId);
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cartId);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(cartEntity.getUser(), userDto); // Populate userDto from CartEntity
        cartResponseDto.setUser(userDto);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        double finalAmount = 0.0;
        double totalSaved = 0.0;

        for (CartItemEntity cartItem : cartItems) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            BeanUtils.copyProperties(cartItem.getProduct(), productResponseDto);
            productResponseDto.setQuantity(cartItem.getQuantity());

            double productFinalPrice = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            double productInitialPrice = cartItem.getProduct().getInitialPrice() * cartItem.getQuantity();

            finalAmount += productFinalPrice;
            totalSaved += (productInitialPrice - productFinalPrice);

            productResponseDtos.add(productResponseDto);
        }

        cartResponseDto.setProducts(productResponseDtos);
        cartResponseDto.setFinalAmount(finalAmount);
        cartResponseDto.setTotalSaved(totalSaved);

        return cartResponseDto;
    }
}
