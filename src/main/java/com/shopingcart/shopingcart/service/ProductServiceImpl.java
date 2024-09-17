package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductDto> products = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            ProductDto product = new ProductDto();
            BeanUtils.copyProperties(productEntity, product);
            product.setImages(Arrays.asList(productEntity.getImages().split(",")));
            products.add(product);
        }
        return products;
    }

    @Override
    public ProductDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if (productEntity != null) {
            ProductDto product = new ProductDto();
            BeanUtils.copyProperties(productEntity, product);
            product.setImages(Arrays.asList(productEntity.getImages().split(",")));
            return product;
        }
        return null;
    }

    @Override
    public ProductDto createProduct(ProductDto product) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        productEntity.setImages(String.join(",", product.getImages()));
        productEntity = productRepository.save(productEntity);
        BeanUtils.copyProperties(productEntity, product);
        return product;
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {
        ProductEntity productEntity = productRepository.findById(product.getId()).orElse(null);
        if (productEntity != null) {
            BeanUtils.copyProperties(product, productEntity);
            productEntity.setImages(String.join(",", product.getImages()));
            productEntity = productRepository.save(productEntity);
            BeanUtils.copyProperties(productEntity, product);
            return product;
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean productExistsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }
}
