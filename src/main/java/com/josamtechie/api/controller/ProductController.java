package com.josamtechie.api.controller;

import com.josamtechie.api.dao.ProductRepository;
import com.josamtechie.api.entity.Product;
import com.josamtechie.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController
{
    private final ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        Product saved = productService.saveProduct(product);
        URI location = URI.create("/product/" + saved.getId());
        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products)
    {
        return ResponseEntity.ok().body(productService.addProducts(products));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id)
    {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<Product> findProductByName(@RequestParam String name)
    {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProducts()
    {
        return ResponseEntity.ok().body(productService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product)
    {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
