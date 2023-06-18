package com.shoppingcartsystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shoppingcartsystem.entities.Category;
import com.shoppingcartsystem.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Page<Product> findByCategory(Category category, Pageable p);

	@Query("SELECT p FROM Product p WHERE CONCAT(' ', LOWER(p.productName), ' ', LOWER(p.productDescription), ' ') LIKE %:keywords%")
	Page<Product> search(@Param("keywords") String keywords, Pageable p);

	Page<Product> findByMerchantId(Integer merchantId, Pageable p);
}
