package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setPicByte(image.getBytes());

        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


    public Product updateProduct(long productId, Product product, MultipartFile image) throws IOException {

        Product product1= productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Product id not found" + productId
        ));

        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setDescription(product.getDescription());


        // Bilddaten nur aktualisieren, wenn ein neues Bild hochgeladen wurde
        if (image != null && !image.isEmpty()) {
            product1.setImageName(image.getOriginalFilename());
            product1.setImageType(image.getContentType());
            product1.setPicByte(image.getBytes());
        }
        return productRepository.save(product1);
    }

    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
    }

    public List<Product> search(String keyword) {
        return productRepository.search(keyword);
    }
}
