package com.shopingcart.shopingcart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CartEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToMany(mappedBy = "cart")
  private List<CartItemEntity> cartItems;
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}