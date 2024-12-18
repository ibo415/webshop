package com.example.product.controller;

import com.example.product.model.Cart;
import com.example.product.model.Product;
import com.example.product.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public List<Product> getCartProducts(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        return cart.getProducts();
    }


    @PostMapping("/{userId}/add/{productId}")
    public Cart addProductToCart(@PathVariable Long userId, @PathVariable Long productId){
        return cartService.addProductToCart(userId,productId);

    }

}
