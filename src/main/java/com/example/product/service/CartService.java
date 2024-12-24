package com.example.product.service;


import com.example.product.model.Cart;
import com.example.product.model.CartProduct;
import com.example.product.model.Product;
import com.example.product.model.Users;
import com.example.product.repository.CartRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UsersRepository usersRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.usersRepository = usersRepository;
    }


    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUsersUserId(userId);

        if (cart == null) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

            cart = new Cart();
            cart.setUsers(user);
            cart.setCartProducts(new ArrayList<>());
            cartRepository.save(cart);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden"));

        for (CartProduct cp : cart.getCartProducts()) {
            if (cp.getProduct().equals(product)) {
                cp.setQuantity(cp.getQuantity() + quantity);
                return cartRepository.save(cart);
            }
        }

        CartProduct newCartProduct = new CartProduct();
        newCartProduct.setCart(cart);
        newCartProduct.setProduct(product);
        newCartProduct.setQuantity(quantity);
        cart.getCartProducts().add(newCartProduct);

        return cartRepository.save(cart);
    }

    //@Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUsersUserId(userId);
    }
}
