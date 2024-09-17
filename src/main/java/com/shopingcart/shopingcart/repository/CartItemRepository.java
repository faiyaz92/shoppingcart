package com.shopingcart.shopingcart.repository;

import com.shopingcart.shopingcart.entity.CartItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends CrudRepository<CartItemEntity, Long> {
    List<CartItemEntity> findAllByCartId(Long cartId);
    CartItemEntity findByCartIdAndProductId(Long cartId, Long productId);
}
