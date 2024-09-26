package com.auto_dily.auto_dily.service;

import com.auto_dily.auto_dily.model.Product;
import com.auto_dily.auto_dily.reopsitory.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private S3Service s3Service;


    public List<Product> allProducts() {
        return productsRepository.findAll();
    }

    public Product getProductById(String id) {
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private final String s3BaseUrl = "https://auto-dily.s3.amazonaws.com/";

    public Product addProduct(Product product, List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageKey = s3Service.uploadFile(image);
            String imageUrl = s3BaseUrl + imageKey;
            imageUrls.add(imageUrl);
        }
        product.setImages(imageUrls.toArray(new String[0]));
        return productsRepository.save(product);
    }

    public Product updateProduct(Product product, List<MultipartFile> newImages, String id) {
        return productsRepository.findById(id)
                .map(existingProduct -> {
                    // Update fields
                    existingProduct.setTitle(product.getTitle());
                    existingProduct.setMake(product.getMake());
                    existingProduct.setModel(product.getModel());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setCurrency(product.getCurrency());

                    //  image updates
                    List<String> currentImages = new ArrayList<>(List.of(existingProduct.getImages()));

                    // Check if newImages is not null or empty
                    if (newImages != null && !newImages.isEmpty()) {
                        for (MultipartFile image : newImages) {
                            if (!image.isEmpty()) {
                                String imageKey = s3Service.uploadFile(image);
                                String imageUrl = s3BaseUrl + imageKey;
                                currentImages.add(imageUrl); // Add new images
                            }
                        }
                    }

                    // Set the updated list of images
                    existingProduct.setImages(currentImages.toArray(new String[0]));

                    return productsRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }



    public void deleteProduct(String id) {
        productsRepository.deleteById(id);

    }
}
