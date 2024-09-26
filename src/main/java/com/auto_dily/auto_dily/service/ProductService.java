package com.auto_dily.auto_dily.service;

import com.auto_dily.auto_dily.model.Product;
import com.auto_dily.auto_dily.reopsitory.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository productsRepository;


    public List<Product> allProducts() {
        return productsRepository.findAll();
    }

    public Product getProductById(String id) {
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }

    public Product updateProduct(Product product, String id) {
        return productsRepository.findById(id)
                .map(p -> {
                    p.setTitle(product.getTitle());
                    p.setMake(product.getMake());
                    p.setModel(product.getModel());
                    p.setDescription(product.getDescription());
                    p.setPrice(product.getPrice());
                    p.setCurrency(product.getCurrency());
                    p.setImages(product.getImages());
                    return productsRepository.save(p);
                }).orElseThrow(RuntimeException::new);
    }

    public void deleteProduct(String id) {
        productsRepository.deleteById(id);

    }
}
