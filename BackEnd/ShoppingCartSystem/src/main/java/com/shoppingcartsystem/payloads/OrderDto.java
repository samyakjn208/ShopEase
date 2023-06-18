package com.shoppingcartsystem.payloads;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

	private int id;
	private String orderId;
	private LocalDate orderDate;
	private String shippedTo;
	private String shippingAddress;
	private String paymentMethod;
	private String paymentDetails;
	private ProductDto product;
	private int quantity;
	private double price;
	private double totalPrice;
	private int sellerId;
	private String sellerName;
	private String orderStatus;
	private UserDto user;

}
