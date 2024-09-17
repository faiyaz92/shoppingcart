package com.shopingcart.shopingcart.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItemEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "cart_id")
  private CartEntity cart;
  
  @ManyToOne
  @JoinColumn(name = "product_id")
  private ProductEntity product;
  
  private Integer quantity;
}
