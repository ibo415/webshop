package com.example.product.service;


import com.example.product.model.Cart;
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


    public Cart addProductToCart(Long userId, Long productId) {
        // Warenkorb des Benutzers abrufen
        Cart cart = cartRepository.findByUsersId(userId);

        if (cart == null) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

            // Warenkorb erstellen und speichern
            cart = new Cart();
            cart.setUsers(user);
            cartRepository.save(cart);  // Speicher den Warenkorb, um die ID zu generieren
        }

        // Produkt suchen
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden"));

        // Produkt dem Warenkorb hinzufügen
        cart.getProducts().add(product);

        // Warenkorb speichern und zurückgeben
        return cartRepository.save(cart);
    }

    //@Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUsersId(userId);
    }
}
