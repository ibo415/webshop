package com.example.product.service;

import com.example.product.model.Users;
import com.example.product.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public Users register(Users user) {
        return usersRepository.save(user);
    }
}
