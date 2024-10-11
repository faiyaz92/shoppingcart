package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.globalerror.ProductAlreadyExistsException;
import com.shopingcart.shopingcart.globalerror.ProductNotFoundException;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testCreateProduct_ThrowsProductAlreadyExistsException() {
        ProductDto productDto = new ProductDto(null, "Product1", 100.0, 120.0, "Sample Product", "250ml", "Manufacturer", "Distributor", null);

        when(productRepository.existsByProductNameAndPackagingQuantity("Product1", "250ml")).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(productDto));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void testGetProductById_Success() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setProductName("Product1");

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        ProductDto productDto = productService.getProductById(1L);

        assertNotNull(productDto);
        assertEquals("Product1", productDto.getProductName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_ThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDto productDto = new ProductDto(1L, "UpdatedProduct", 150.0, 170.0, "Updated Description", "500ml", "Manufacturer", "Distributor", null);
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsByProductNameAndPackagingQuantity("UpdatedProduct", "500ml")).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(existingProduct);

        ProductDto updatedProduct = productService.updateProduct(productDto);

        assertNotNull(updatedProduct);
        assertEquals("UpdatedProduct", updatedProduct.getProductName());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void testUpdateProduct_ThrowsProductNotFoundException() {
        ProductDto productDto = new ProductDto(1L, "UpdatedProduct", 150.0, 170.0, "Updated Description", "500ml", "Manufacturer", "Distributor", null);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productDto));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ThrowsProductNotFoundException() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(1L);
    }

    @Test
    void testProductExistsByNameAndPackaging_Success() {
        when(productRepository.existsByProductNameAndPackagingQuantity("Product1", "250ml")).thenReturn(true);

        boolean exists = productRepository.existsByProductNameAndPackagingQuantity("Product1", "250ml");

        assertTrue(exists);
        verify(productRepository, times(1)).existsByProductNameAndPackagingQuantity("Product1", "250ml");
    }

    @Test
    void testCreateProduct_Success() {
        ProductDto productDto = new ProductDto();
        productDto.setProductName("Product1");
        productDto.setPackagingQuantity("250ml");
        productDto.setPrice(100.0);
        productDto.setInitialPrice(120.0);
        productDto.setDescription("Description");
        productDto.setManufacturerName("Manufacturer");
        productDto.setDistributorName("Distributor");
        productDto.setImages(Arrays.asList("image1.png", "image2.png"));

        when(productRepository.existsByProductNameAndPackagingQuantity(anyString(), anyString())).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(new ProductEntity());

        ProductDto createdProduct = productService.createProduct(productDto);

        assertNotNull(createdProduct);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

}
