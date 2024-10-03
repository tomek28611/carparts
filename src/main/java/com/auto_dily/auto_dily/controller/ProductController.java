package com.auto_dily.auto_dily.controller;

import com.auto_dily.auto_dily.model.Product;

import com.auto_dily.auto_dily.reopsitory.SearchRepository;
import com.auto_dily.auto_dily.service.ProductService;
import com.auto_dily.auto_dily.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    S3Service s3Service;

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

    @PostMapping("/admin/products/add")
    public Product addProduct(
            @RequestPart("product") String productJson,
            @RequestPart("images") List<MultipartFile> images) {

        System.out.println("Received product JSON: " + productJson);
        System.out.println("Number of images received: " + images.size());

        ObjectMapper objectMapper = new ObjectMapper();
        Product product;

        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing product JSON: " + e.getMessage(), e);
        }

        return productService.addProduct(product, images);
    }



    @PutMapping("/admin/products/{id}")
    public Product updateProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @PathVariable String id) {

        ObjectMapper objectMapper = new ObjectMapper();
        Product product;

        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing product JSON: " + e.getMessage(), e);
        }

        return productService.updateProduct(product, images, id);
    }


    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
