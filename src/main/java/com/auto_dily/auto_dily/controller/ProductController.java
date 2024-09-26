package com.auto_dily.auto_dily.controller;

import com.auto_dily.auto_dily.model.Product;

import com.auto_dily.auto_dily.reopsitory.SearchRepository;
import com.auto_dily.auto_dily.service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SearchRepository searchRepository;

    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<List<Product>>(productService.allProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products/{text}")
    public List<Product> search(@PathVariable String text) {
        return searchRepository.findByText(text);
    }

    @PostMapping("/products/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable String id) {
        return productService.updateProduct(product, id);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }




}
