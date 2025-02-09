package com.example.product.repository;

import com.example.product.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Users findByUsername(String username);
}
