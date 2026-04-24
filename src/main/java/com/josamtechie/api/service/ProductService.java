package com.josamtechie.api.service;

import com.josamtechie.api.dao.ProductRepository;
import com.josamtechie.api.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;

    public Product saveProduct(Product product)
    {
        //log.info("Saving product {}", product.getId());
        System.out.println("Saving product : " + product.getId());
        boolean validFlag = validateProductName(product.getName());
        if (validFlag) {
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product name is invalid");
        }
    }

    public List<Product> addProducts(List<Product> products)
    {
        log.info("Adding products {}", products);
        return productRepository.saveAll(products);
    }

    public List<Product> findAll()
    {
        log.info("Finding all products");
        return productRepository.findAll();
    }

    public Product findById(Long id)
    {
        log.info("Finding product by id {}", id);
        return productRepository.findById(id).orElse(null);
    }

    public Product findByName(String name)
    {
        log.info("Finding product by name {}", name);
        return productRepository.findByName(name).orElse(null);
    }

    public Product updateProduct(Long id, Product product)
    {
        log.info("Updating product {}", product);
        Product oldProduct = productRepository.findById(id).orElse(null);
        if (oldProduct != null) {
            oldProduct.setName(product.getName());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setQty(product.getQty());
        }
        assert oldProduct != null;
        return productRepository.save(oldProduct);
    }

    public void deleteProduct(Long id)
    {
        //log.info("Deleting product by id {}", id);
        System.out.println("Deleting product by id : " + id);
        productRepository.deleteById(id);
    }

    private boolean validateProductName(String name)
    {
        return name != null && !name.isEmpty();
    }

}
