package com.example.product.controller;

import com.example.product.model.Cart;
import com.example.product.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("{userId}")
    public Cart getCartByUserId(@PathVariable Long userId){
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add/{productId}")
    public Cart addProductToCart(@PathVariable Long userId, @PathVariable Long productId){
        return cartService.addProductToCart(userId,productId);

    }

}
