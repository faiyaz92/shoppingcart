package com.shopingcart.shopingcart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopingcart.shopingcart.controller.ProductController;
import com.shopingcart.shopingcart.data.ProductDto;
import com.shopingcart.shopingcart.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private static final String API_URL = "/api/products";
    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Test Product";
    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    private ProductDto getProduct() {
        return new ProductDto(PRODUCT_ID, PRODUCT_NAME, 10.99, 15.99, "Test description", "500ml", "Manufacturer", "Distributor", List.of("image1.jpg", "image2.jpg"));
    }

    private String asJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<ProductDto> products = new ArrayList<>();
        products.add(getProduct());
        products.add(new ProductDto(2L, "Product 2", 9.99, 14.99, "Description 2", "250ml", "Manufacturer 2", "Distributor 2", List.of("image3.jpg", "image4.jpg")));
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetProductByIdExisting() throws Exception {
        ProductDto product = getProduct();
        when(productService.getProductById(PRODUCT_ID)).thenReturn(product);

        mockMvc.perform(get(API_URL + "/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(PRODUCT_ID.intValue())))
                .andExpect(jsonPath("$.productName").value(is(PRODUCT_NAME)));
    }

    @Test
    public void testGetProductByIdNonExisting() throws Exception {
        when(productService.getProductById(PRODUCT_ID)).thenReturn(null);

        mockMvc.perform(get(API_URL + "/" + PRODUCT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDto product = getProduct();
        when(productService.createProduct(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(post(API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(PRODUCT_ID.intValue())))
                .andExpect(jsonPath("$.productName").value(is(PRODUCT_NAME)));
    }

    @Test
    public void testUpdateProductExisting() throws Exception {
        ProductDto product = getProduct();
        when(productService.updateProduct(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(put(API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(PRODUCT_ID.intValue())))
                .andExpect(jsonPath("$.productName").value(is(PRODUCT_NAME)));
    }

    @Test
    public void testUpdateProductNonExisting() throws Exception {
        when(productService.updateProduct(any(ProductDto.class))).thenReturn(null);

        mockMvc.perform(put(API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(getProduct())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProductExisting() throws Exception {
        doNothing().when(productService).deleteProduct(PRODUCT_ID);

        mockMvc.perform(delete(API_URL + "/" + PRODUCT_ID))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testDeleteProductNonExisting() throws Exception {
        doThrow(new RuntimeException()).when(productService).deleteProduct(PRODUCT_ID);

        mockMvc.perform(delete(API_URL + "/" + PRODUCT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testProductExistsTrue() throws Exception {
        when(productService.productExists(PRODUCT_ID)).thenReturn(true);

        mockMvc.perform(get(API_URL + "/exists/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(is(true)));
    }

    @Test
    public void testProductExistsFalse() throws Exception {
        when(productService.productExists(PRODUCT_ID)).thenReturn(false);

        mockMvc.perform(get(API_URL + "/exists/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(is(false)));
    }

    @Test
    public void testProductExistsByProductNameTrue() throws Exception {
        when(productService.productExistsByProductName(PRODUCT_NAME)).thenReturn(true);

        mockMvc.perform(get(API_URL + "/exists/name/" + PRODUCT_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(is(true)));
    }

    @Test
    public void testProductExistsByProductNameFalse() throws Exception {
        when(productService.productExistsByProductName(PRODUCT_NAME)).thenReturn(false);

        mockMvc.perform(get(API_URL + "/exists/name/" + PRODUCT_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").value(is(false)));
    }
}
