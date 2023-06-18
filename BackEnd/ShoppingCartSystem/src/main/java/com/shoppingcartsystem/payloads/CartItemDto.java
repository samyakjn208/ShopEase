package com.shoppingcartsystem.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartItemDto {

	private int cartItemId;
	private ProductDto product;
	private int quantity;
	private double totalProductPrice;
}
