package com.shoppingcartsystem.services;

import com.shoppingcartsystem.payloads.CartDto;

public interface CartService {

	CartDto addToCart(Integer userId, Integer productId);
	
	//For decreasing quantity of product in cart
	CartDto removeFromCart(Integer userId, Integer productId);
	
	CartDto viewCart(Integer userId);
	
	void deleteCart(Integer userId);
	
	CartDto removeProductFromCart(Integer userId, Integer productId);
}
