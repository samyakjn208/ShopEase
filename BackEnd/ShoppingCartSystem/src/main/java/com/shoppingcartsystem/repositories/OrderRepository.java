package com.shoppingcartsystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsystem.entities.Order;
import com.shoppingcartsystem.entities.User;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Page<Order> findByUser(User user, Pageable p);
	Page<Order> findBySellerId(Integer sellerId, Pageable p);
}
