package com.shoppingcartsystem.exceptions;

public class OutOfStockException extends RuntimeException {

	public OutOfStockException() {
		super(String.format("One on more product(s) in your cart is no more in stock"));
	}
}
