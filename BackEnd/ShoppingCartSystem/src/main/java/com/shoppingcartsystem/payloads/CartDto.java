package com.shoppingcartsystem.payloads;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartDto {

	private int cartId;
	private List<CartItemDto> cartItem = new ArrayList<>();
	private UserDto user;
	private double grandTotal;
}
