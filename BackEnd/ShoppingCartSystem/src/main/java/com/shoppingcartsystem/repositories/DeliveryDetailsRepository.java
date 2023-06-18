package com.shoppingcartsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsystem.entities.DeliveryDetails;
import com.shoppingcartsystem.entities.User;

public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Integer>{
	List<DeliveryDetails> findByUser(User user);

}
