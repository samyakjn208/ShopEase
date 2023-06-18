package com.shoppingcartsystem.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	private int cartId;				//For cart check out
	private int productId;			//For single product purchase
	private int userId;
	private int deliveryDetailsId;
	private String paymentMethod;
	private String paymentDetails;
	private String orderStatus;
	
}
