package com.shopingcart.shopingcart.service;

import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.globalerror.ProductAlreadyExistsException;
import com.shopingcart.shopingcart.globalerror.ProductNotFoundException;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.responses.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Check if a product with the same name and packaging already exists
        if (productRepository.existsByProductNameAndPackagingQuantity(productDto.getProductName(), productDto.getPackagingQuantity())) {
            throw new ProductAlreadyExistsException("Product with name " + productDto.getProductName() +
                                                    " and packaging " + productDto.getPackagingQuantity() + " already exists.");
        }

        // Map ProductDto to ProductEntity
        ProductEntity productEntity = convertToEntity(productDto);
        ProductEntity savedEntity = productRepository.save(productEntity);

        // Return the saved ProductDto
        return convertToDto(savedEntity);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        // Check if the product exists
        ProductEntity existingProduct = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productDto.getId()));

        // Update the existing product's fields
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setInitialPrice(productDto.getInitialPrice());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPackagingQuantity(productDto.getPackagingQuantity());
        existingProduct.setManufacturerName(productDto.getManufacturerName());
        existingProduct.setDistributorName(productDto.getDistributorName());

        // Check if the images list is not null and handle accordingly
        if (productDto.getImages() != null) {
            existingProduct.setImages(String.join(",", productDto.getImages())); // Join images into a comma-separated string
        } else {
            existingProduct.setImages(""); // Set to empty string or null based on your requirements
        }

        ProductEntity updatedEntity = productRepository.save(existingProduct);
        return convertToDto(updatedEntity);
    }





    @Override
    public SuccessResponse deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }

        productRepository.deleteById(id);

        // Return success response on successful deletion
        return new SuccessResponse("Product deleted successfully", HttpStatus.OK.value());
    }

    @Override
    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean productExistsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    // Utility methods to convert between DTO and Entity
    private ProductDto convertToDto(ProductEntity productEntity) {
        ProductDto productDto = new ProductDto();
        productDto.setId(productEntity.getId());
        productDto.setProductName(productEntity.getProductName());
        productDto.setPrice(productEntity.getPrice());
        productDto.setInitialPrice(productEntity.getInitialPrice());
        productDto.setDescription(productEntity.getDescription());
        productDto.setPackagingQuantity(productEntity.getPackagingQuantity());
        productDto.setManufacturerName(productEntity.getManufacturerName());
        productDto.setDistributorName(productEntity.getDistributorName());

        // Safely handle the images field
        String images = productEntity.getImages();
        if (images != null && !images.isEmpty()) {
            productDto.setImages(List.of(images.split(","))); // Convert string back to list
        } else {
            productDto.setImages(Collections.emptyList()); // Set to empty list if null or empty
        }

        return productDto;
    }

    private ProductEntity convertToEntity(ProductDto productDto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(productDto.getProductName());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setInitialPrice(productDto.getInitialPrice());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setPackagingQuantity(productDto.getPackagingQuantity());
        productEntity.setManufacturerName(productDto.getManufacturerName());
        productEntity.setDistributorName(productDto.getDistributorName());
        productEntity.setImages(String.join(",", productDto.getImages())); // Convert list to comma-separated string
        return productEntity;
    }
}
