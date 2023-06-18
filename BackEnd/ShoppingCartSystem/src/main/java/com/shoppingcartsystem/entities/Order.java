package com.shoppingcartsystem.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 15)
	private String orderId;

	private LocalDate orderDate;

	@Column(length = 20)
	private String shippedTo;

	@Column(length = 200)
	private String shippingAddress;

	@Column(length = 10)
	private String paymentMethod;

	@Column(length = 100)
	private String paymentDetails;

	@OneToOne
	@JoinColumn(name = "productId")
	private Product product;

	@Column(length = 2)
	private int quantity;

	@Column(length = 10)
	private double price;

	@Column(length = 10)
	private double totalPrice;

	@Column(length = 10, nullable = false)
	private int sellerId;

	@Column(length = 20, nullable = false)
	private String sellerName;

	@Column(length = 250)
	private String orderStatus;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

}
