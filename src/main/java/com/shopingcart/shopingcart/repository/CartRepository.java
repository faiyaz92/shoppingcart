package com.shopingcart.shopingcart.repository;

import com.shopingcart.shopingcart.entity.CartEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, Long> {
}
