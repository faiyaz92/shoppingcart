package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.controller.ProductController;
import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.responses.SuccessResponse;
import com.shopingcart.shopingcart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_Success() {
        ProductDto product1 = new ProductDto();
        ProductDto product2 = new ProductDto();
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<List<ProductDto>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        ProductDto productDto = new ProductDto();
        when(productService.getProductById(productId)).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(null);

        ResponseEntity<ProductDto> response = productController.getProductById(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testCreateProduct_Success() {
        ProductDto productDto = new ProductDto();
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productController.createProduct(productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDto productDto = new ProductDto();
        when(productService.updateProduct(any(ProductDto.class))).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productController.updateProduct(productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService, times(1)).updateProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        ProductDto productDto = new ProductDto();
        when(productService.updateProduct(any(ProductDto.class))).thenReturn(null);

        ResponseEntity<ProductDto> response = productController.updateProduct(productDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).updateProduct(any(ProductDto.class));
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;
        SuccessResponse successResponse = new SuccessResponse("Product deleted successfully", HttpStatus.OK.value());
        when(productService.deleteProduct(productId)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void testProductExists_Success() {
        Long productId = 1L;
        when(productService.productExists(productId)).thenReturn(true);

        ResponseEntity<Boolean> response = productController.productExists(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(productService, times(1)).productExists(productId);
    }

    @Test
    void testProductExistsByProductName_Success() {
        String productName = "Test Product";
        when(productService.productExistsByProductName(productName)).thenReturn(true);

        ResponseEntity<Boolean> response = productController.productExistsByProductName(productName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(productService, times(1)).productExistsByProductName(productName);
    }
}
