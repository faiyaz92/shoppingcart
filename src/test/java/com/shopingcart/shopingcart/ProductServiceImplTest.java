package com.shopingcart.shopingcart;

import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.entity.ProductEntity;
import com.shopingcart.shopingcart.repository.ProductRepository;
import com.shopingcart.shopingcart.service.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto product;
    private ProductEntity productEntity;

    @Before
    public void setup() {
        product = new ProductDto(1L, "Test Product", 10.99, 15.99, "Test description", "500ml", "Manufacturer", "Distributor", List.of("image1.jpg", "image2.jpg"));
        productEntity = new ProductEntity();
        BeanUtils.copyProperties(product, productEntity);
        productEntity.setImages(String.join(",", product.getImages()));
    }

    @Test
    public void testGetAllProducts() {
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(productEntity);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setProductName("Product 2");
        productEntity2.setPrice(9.99);
        productEntity2.setInitialPrice(14.99);
        productEntity2.setDescription("Description 2");
        productEntity2.setPackagingQuantity("250ml");
        productEntity2.setManufacturerName("Manufacturer 2");
        productEntity2.setDistributorName("Distributor 2");
        productEntity2.setImages("image3.jpg,image4.jpg");

        productEntities.add(productEntity2);
        when(productRepository.findAll()).thenReturn(productEntities);

        List<ProductDto> products = productService.getAllProducts();
        assertEquals(2, products.size());
        assertEquals(product.getProductName(), products.get(0).getProductName());
    }

    @Test
    public void testGetProductByIdExisting() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(productEntity));

        ProductDto retrievedProduct = productService.getProductById(product.getId());
        assertEquals(product.getProductName(), retrievedProduct.getProductName());
    }

    @Test
    public void testGetProductByIdNonExisting() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        ProductDto retrievedProduct = productService.getProductById(product.getId());
        assertNull(retrievedProduct);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDto createdProduct = productService.createProduct(product);
        assertEquals(product.getProductName(), createdProduct.getProductName());
    }

    @Test
    public void testUpdateProductExisting() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDto updatedProduct = productService.updateProduct(product);
        assertEquals(product.getProductName(), updatedProduct.getProductName());
    }

    @Test
    public void testUpdateProductNonExisting() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        ProductDto updatedProduct = productService.updateProduct(product);
        assertNull(updatedProduct);
    }

    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(product.getId());
        verify(productRepository, times(1)).deleteById(product.getId());
    }

    @Test
    public void testProductExistsTrue() {
        when(productRepository.existsById(product.getId())).thenReturn(true);

        boolean exists = productService.productExists(product.getId());
        assertEquals(true, exists);
    }

    @Test
    public void testProductExistsFalse() {
        when(productRepository.existsById(product.getId())).thenReturn(false);

        boolean exists = productService.productExists(product.getId());
        assertEquals(false, exists);
    }

    @Test
    public void testProductExistsByProductNameTrue() {
        when(productRepository.existsByProductName(product.getProductName())).thenReturn(true);

        boolean exists = productService.productExistsByProductName(product.getProductName());
        assertEquals(true, exists);
    }

    @Test
    public void testProductExistsByProductNameFalse() {
        when(productRepository.existsByProductName(product.getProductName())).thenReturn(false);

        boolean exists = productService.productExistsByProductName(product.getProductName());
        assertEquals(false, exists);
    }
}