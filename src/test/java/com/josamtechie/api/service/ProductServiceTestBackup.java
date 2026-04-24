package com.josamtechie.api.service;

import com.josamtechie.api.dao.ProductRepository;
import com.josamtechie.api.entity.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTestBackup
{
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    static Product product = null;

    // Expensive Resource Initialization
    // Example:
    // 1. Database connection
    // 2. Embedded server
    // 3. Large object graph
    @BeforeAll
    public static void init()
    {
        System.out.println("BeforeAll");
        // Data preparation
        product = new Product();
        product.setId(1L);
        product.setName("Test product name"); //Test product name
        product.setDescription("Test product description");
        product.setPrice(88.99);
        product.setQty(7);
    }

    @BeforeEach
    public void initEachTest()
    {
        System.out.println("BeforeEach");
    }

    @Test
    void addProductSuccessfully()
    {
        //  Data preparation - Moved to @BeforeAll
        //  Product product = new Product();
        //  product.setId(1L);
        //  product.setName("Test product name"); //Test product name
        //  product.setDescription("Test product description");
        //  product.setPrice(88.99);
        //  product.setQty(7);

        // Mocking the calls if any
        when(productRepository.save(product)).thenReturn(product);

        // Calling the actual method
        Product savedProduct = productService.saveProduct(product);

        //  Assertions
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertTrue(product.getId() == 1);
        //  assertEquals(2, savedProduct.getId()); // Negative scenario
    }

    @Test
    void addProductThrowExceptionForInvalidProductName()
    {
        //  Data preparation
        //  Product product = new Product();
        //  product.setId(1L);
        //  product.setName(""); //Test product name
        //  product.setDescription("Test product description");
        //  product.setPrice(88.99);
        //  product.setQty(7);
        product.setName("");
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            productService.saveProduct(product);
        });
        assertEquals("Product name is invalid", runtimeException.getMessage());
        //  assertEquals("Product names is invalid", runtimeException.getMessage()); // Negative scenario
        verify(productRepository, times(0)).save(product);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProductSuccessfully()
    {
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @AfterAll
    public static void terminate()
    {
        System.out.println("AfterAll");
    }

    @AfterEach
    public void cleanupTest()
    {
        System.out.println("AfterEach");
    }

    // Using java reflections
    @Test
    void testPrivateMethodValidProductName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method validateProductName = ProductService.class.getDeclaredMethod("validateProductName", String.class);
        validateProductName.setAccessible(true);
        Boolean testProductName = (Boolean) validateProductName.invoke(productService, "Test product name");
        assertTrue(testProductName);
    }

    @Test
    void testPrivateMethodInValidProductName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Method validateProductName = ProductService.class.getDeclaredMethod("validateProductName", String.class);
        validateProductName.setAccessible(true);
        Boolean testProductName = (Boolean) validateProductName.invoke(productService, "");
        assertFalse(testProductName);
    }
}