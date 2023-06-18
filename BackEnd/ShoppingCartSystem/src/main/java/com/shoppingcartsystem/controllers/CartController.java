package com.shoppingcartsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcartsystem.payloads.ApiResponse;
import com.shoppingcartsystem.payloads.CartDto;
import com.shoppingcartsystem.payloads.SessionCart;
import com.shoppingcartsystem.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private SessionCart sessionCart;

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/addToCart")
	CartDto addToCart(@RequestParam(required = false) Integer userId, @RequestParam Integer productId) {
		if (userId == null) {
			return this.sessionCart.addToCart(productId);
		} else {
			return this.cartService.addToCart(userId, productId);
		}
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/removeFromCart")
	CartDto removeFromCart(@RequestParam(required = false) Integer userId, @RequestParam Integer productId) {
		if(userId ==  null) {
			return this.sessionCart.removeFromCart(productId);
		}else {
			return this.cartService.removeFromCart(userId, productId);
		}
	}
	
	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/removeProductFromCart")
	CartDto removeProductFromCart(@RequestParam(required = false) Integer userId, @RequestParam Integer productId) {
		if(userId ==  null) {
			return this.sessionCart.removeFromCart(productId);
		}else {
			return this.cartService.removeProductFromCart(userId, productId);
		}
	}
	
	@PreAuthorize("hasAnyAuthority('USER')")
	@GetMapping("/viewCart")
	CartDto viewCart(@RequestParam(required = false) Integer userId) {
		if (userId == null) {
			return this.sessionCart.getCartItems();
		} else {
			return this.cartService.viewCart(userId);
		}
	}

	@PreAuthorize("hasAnyAuthority('USER')")
	@DeleteMapping("/deleteCart")
	public ResponseEntity<ApiResponse> deleteCart(@RequestParam(required = false) Integer userId){
		if(userId == null) {
			this.sessionCart.deleteCart();
		}else {
			this.cartService.deleteCart(userId);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("Cart Deleted Successfully!", true), HttpStatus.OK);
	}
}
