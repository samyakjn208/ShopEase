package com.shoppingcartsystem.payloads;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.shoppingcartsystem.exceptions.ResourceNotFoundException;
import com.shoppingcartsystem.services.ProductService;

@Component
@SessionScope
public class SessionCart {

	@Autowired
	private ProductService productService;

	private List<CartItemDto> cartItems = new ArrayList<>();

	public CartDto addToCart(Integer requiredProductId) {
		ProductDto productDto = this.productService.getProductById(requiredProductId);
		
		Optional<CartItemDto> existingCartItemDto = cartItems.stream()
				.filter(item -> item.getProduct().getProductId() == requiredProductId).findFirst();
		
		if(existingCartItemDto.isPresent()) {
			CartItemDto cartItemDto = existingCartItemDto.get();
			cartItemDto.setQuantity(cartItemDto.getQuantity()+1);
			cartItemDto.setTotalProductPrice(cartItemDto.getProduct().getProductPrice()*cartItemDto.getQuantity());
		}else {
			CartItemDto cartItemDto = new CartItemDto();

			cartItemDto.setProduct(productDto);
			cartItemDto.setQuantity(1);
			cartItemDto.setTotalProductPrice(productDto.getProductPrice());
			
			cartItems.add(cartItemDto);
		}
		CartDto cartDto = new CartDto();
		cartDto.setCartItem(cartItems);

		return cartDto;
	}

	
	public CartDto removeFromCart(Integer requiredProductId) {
		Optional<CartItemDto> existingCartItemDto = cartItems.stream()
				.filter(item -> item.getProduct().getProductId() == requiredProductId).findFirst();
		
		if(existingCartItemDto.isEmpty()) {
			throw new ResourceNotFoundException("CartItem", "ProductId", requiredProductId);
		}
		
		CartItemDto cartItemDto = existingCartItemDto.get();
		if(cartItemDto.getQuantity()==1) {
			cartItems.remove(cartItemDto);
		}
		cartItemDto.setQuantity(cartItemDto.getQuantity()-1);
		cartItemDto.setTotalProductPrice(cartItemDto.getProduct().getProductPrice()*cartItemDto.getQuantity());
		
		CartDto cartDto = new CartDto();
		cartDto.setCartItem(cartItems);
		
		return cartDto;
	}
	
	public CartDto getCartItems() {
		CartDto cartDto = new CartDto();
		cartDto.setCartItem(cartItems);
		return cartDto;
	}


	public void deleteCart() {
		if(cartItems.isEmpty()) {
			throw new ResourceNotFoundException("Cart", "CartId", 0);
		}
		cartItems.clear();
	}
}
