package com.example.product.service;

import com.example.product.model.Cart;
import com.example.product.model.Users;
import com.example.product.repository.CartRepository;
import com.example.product.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserService {


    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CartRepository cartRepository;

    public UserService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, CartRepository cartRepository) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cartRepository = cartRepository;
    }

    public Users register(Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser = usersRepository.save(user);

        Cart cart = new Cart();
        cart.setUsers(savedUser);
        cart.setProducts(new ArrayList<>());
        cartRepository.save(cart);
        savedUser.setCart(cart);
        return  usersRepository.save(savedUser);
    }

    public String verify(Users user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authenticate.isAuthenticated())
            return jwtService.generateToken(authenticate);
        return "failure";
    }
}
