package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.ProductDto;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(ProductDto product);
    void deleteProduct(Long id);
    boolean productExists(Long id);
    boolean productExistsByProductName(String productName);
}