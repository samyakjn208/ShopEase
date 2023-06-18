package com.shoppingcartsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsystem.entities.Cart;
import com.shoppingcartsystem.entities.CartItem;
import com.shoppingcartsystem.entities.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	CartItem findByProductAndCart(Product product, Cart cart);
}
