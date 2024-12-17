package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {


    private final ProductService productService;

    ProductController(ProductService ps){
        this.productService  = ps;

    }

    @RequestMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable long id){
        return productService.getProductById(id);
    }

    @PostMapping(path = "/products", consumes={"multipart/form-data"})
    public Product createProduct(@RequestPart("product") Product product, @RequestPart(value = "image") MultipartFile image) throws IOException {
        return productService.createProduct(product,image);

    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable long id,
                                 @RequestPart Product product,
                                 @RequestPart(required = false) MultipartFile image) throws IOException {
        return productService.updateProduct(id, product, image);
    }

    @GetMapping("products/keyword")
    public List<Product> searchProdukt(@RequestParam String keyword){
        return productService.search(keyword);

    }

    //DELETE


}
