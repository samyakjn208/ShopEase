package com.shoppingcartsystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsystem.entities.Cart;
import com.shoppingcartsystem.entities.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Optional<Cart> findByUser(User user);
}
