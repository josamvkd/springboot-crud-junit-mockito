package com.josamtechie.api.service;

import com.josamtechie.api.dao.ProductRepository;
import com.josamtechie.api.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        product.setQty(10);
    }

    // -------------------------------------------------------------------------
    // saveProduct
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("saveProduct()")
    class SaveProduct {

        @Test
        @DisplayName("Should save and return product when name is valid")
        void shouldSaveProductWhenNameIsValid() {
            given(productRepository.save(product)).willReturn(product);

            Product saved = productService.saveProduct(product);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isEqualTo(1L);
            assertThat(saved.getName()).isEqualTo("Test Product");
            then(productRepository).should(times(1)).save(product);
        }

        @Test
        @DisplayName("Should throw RuntimeException when product name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            product.setName(null);

            assertThatThrownBy(() -> productService.saveProduct(product))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Product name is invalid");

            then(productRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("Should throw RuntimeException when product name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            product.setName("");

            assertThatThrownBy(() -> productService.saveProduct(product))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Product name is invalid");

            then(productRepository).should(never()).save(any());
        }
    }

    // -------------------------------------------------------------------------
    // addProducts
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("addProducts()")
    class AddProducts {

        @Test
        @DisplayName("Should save and return all products in the list")
        void shouldSaveAllProducts() {
            Product product2 = new Product();
            product2.setId(2L);
            product2.setName("Product 2");

            List<Product> products = List.of(product, product2);
            given(productRepository.saveAll(products)).willReturn(products);

            List<Product> result = productService.addProducts(products);

            assertThat(result).hasSize(2).containsExactlyInAnyOrder(product, product2);
            then(productRepository).should(times(1)).saveAll(products);
        }

        @Test
        @DisplayName("Should return empty list when input is empty")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            List<Product> empty = List.of();
            given(productRepository.saveAll(empty)).willReturn(empty);

            List<Product> result = productService.addProducts(empty);

            assertThat(result).isEmpty();
            then(productRepository).should(times(1)).saveAll(empty);
        }
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("Should return all products from repository")
        void shouldReturnAllProducts() {
            given(productRepository.findAll()).willReturn(List.of(product));

            List<Product> result = productService.findAll();

            assertThat(result).hasSize(1).contains(product);
            then(productRepository).should(times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void shouldReturnEmptyListWhenNoProducts() {
            given(productRepository.findAll()).willReturn(List.of());

            List<Product> result = productService.findAll();

            assertThat(result).isEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("Should return product when found by id")
        void shouldReturnProductWhenFound() {
            given(productRepository.findById(1L)).willReturn(Optional.of(product));

            Product result = productService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            then(productRepository).should(times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return null when product not found by id")
        void shouldReturnNullWhenNotFound() {
            given(productRepository.findById(anyLong())).willReturn(Optional.empty());

            Product result = productService.findById(99L);

            assertThat(result).isNull();
        }
    }

    // -------------------------------------------------------------------------
    // findByName
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("findByName()")
    class FindByName {

        @Test
        @DisplayName("Should return product when found by name")
        void shouldReturnProductWhenFoundByName() {
            given(productRepository.findByName("Test Product")).willReturn(Optional.of(product));

            Product result = productService.findByName("Test Product");

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Test Product");
            then(productRepository).should(times(1)).findByName("Test Product");
        }

        @Test
        @DisplayName("Should return null when product not found by name")
        void shouldReturnNullWhenNotFoundByName() {
            given(productRepository.findByName("Unknown")).willReturn(Optional.empty());

            Product result = productService.findByName("Unknown");

            assertThat(result).isNull();
        }
    }

    // -------------------------------------------------------------------------
    // updateProduct
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("updateProduct()")
    class UpdateProduct {

        @Test
        @DisplayName("Should update and return product when it exists")
        void shouldUpdateProductWhenExists() {
            Product updatedDetails = new Product();
            updatedDetails.setName("Updated Name");
            updatedDetails.setDescription("Updated Description");
            updatedDetails.setPrice(199.99);
            updatedDetails.setQty(5);

            Product savedProduct = new Product();
            savedProduct.setId(1L);
            savedProduct.setName("Updated Name");
            savedProduct.setDescription("Updated Description");
            savedProduct.setPrice(199.99);
            savedProduct.setQty(5);

            given(productRepository.findById(1L)).willReturn(Optional.of(product));
            given(productRepository.save(any(Product.class))).willReturn(savedProduct);

            Product result = productService.updateProduct(1L, updatedDetails);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Name");
            assertThat(result.getDescription()).isEqualTo("Updated Description");
            assertThat(result.getPrice()).isEqualTo(199.99);
            assertThat(result.getQty()).isEqualTo(5);
            then(productRepository).should(times(1)).findById(1L);
            then(productRepository).should(times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw AssertionError when product does not exist")
        void shouldThrowWhenProductNotFound() {
            given(productRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(99L, product))
                    .isInstanceOf(AssertionError.class);

            then(productRepository).should(never()).save(any());
        }
    }

    // -------------------------------------------------------------------------
    // deleteProduct
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProduct {

        @Test
        @DisplayName("Should call repository deleteById with correct id")
        void shouldDeleteProductById() {
            doNothing().when(productRepository).deleteById(1L);

            productService.deleteProduct(1L);

            then(productRepository).should(times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should propagate exception when repository throws")
        void shouldPropagateExceptionFromRepository() {
            doThrow(new RuntimeException("Delete failed"))
                    .when(productRepository).deleteById(99L);

            assertThatThrownBy(() -> productService.deleteProduct(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Delete failed");
        }
    }
}