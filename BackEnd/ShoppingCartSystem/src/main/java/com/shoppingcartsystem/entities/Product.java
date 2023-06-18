package com.shoppingcartsystem.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;

	@Column(name = "product_name", nullable = false, length = 1000)
	private String productName;

	@Column(name = "product_description", nullable = false, length = 100000)
	private String productDescription;

	@Column(name = "product_photo")
	private List<String> productPhoto;

	@Column(name = "product_MRP", length = 10)
	private double productMRP;

	@Column(name = "product_price", nullable = false, length = 10)
	private double productPrice;

	@Column(name = "stock", length = 10)
	private Integer stock;

	@Column(name = "merchantId", length = 10)
	private Integer merchantId;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

}
