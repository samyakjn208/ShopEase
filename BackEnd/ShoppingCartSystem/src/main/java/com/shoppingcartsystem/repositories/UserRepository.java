package com.shoppingcartsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsystem.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmailId(String emailId);

	User findByEmailIdAndPassword(String emailId, String password);
	
	List<User> findByRole(String role);
}
