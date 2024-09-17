package com.shopingcart.shopingcart.repository;

import com.shopingcart.shopingcart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}