package com.shopingcart.shopingcart.repository;

import com.shopingcart.shopingcart.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByProductName(String productName);
    boolean existsByProductNameAndPackagingQuantity(String productName, String packagingQuantity);

}
