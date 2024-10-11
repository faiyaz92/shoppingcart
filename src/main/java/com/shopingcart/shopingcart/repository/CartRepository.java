package com.shopingcart.shopingcart.repository;

import com.shopingcart.shopingcart.entity.CartEntity;
import com.shopingcart.shopingcart.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends CrudRepository<CartEntity, Long> {
    CartEntity findByUser(UserEntity user);


}
