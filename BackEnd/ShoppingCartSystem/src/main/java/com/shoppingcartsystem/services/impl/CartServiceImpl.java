package com.shoppingcartsystem.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingcartsystem.entities.Product;
import com.shoppingcartsystem.entities.User;
import com.shoppingcartsystem.entities.Cart;
import com.shoppingcartsystem.entities.CartItem;
import com.shoppingcartsystem.exceptions.ResourceNotFoundException;
import com.shoppingcartsystem.payloads.CartDto;
import com.shoppingcartsystem.repositories.CartItemRepository;
import com.shoppingcartsystem.repositories.CartRepository;
import com.shoppingcartsystem.repositories.ProductRepository;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CartDto addToCart(Integer userId, Integer productId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));

		Cart cart = this.cartRepository.findByUser(user).orElse(null);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cart.setGrandTotal(0);
			cart = this.cartRepository.save(cart);
		}

		CartItem cartItem = this.cartItemRepository.findByProductAndCart(product, cart);
		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			cartItem.setTotalProductPrice(product.getProductPrice());
			cartItem.setCart(cart);
			cart.setGrandTotal(cart.getGrandTotal() + cartItem.getProduct().getProductPrice());
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItem.setTotalProductPrice(cartItem.getQuantity() * product.getProductPrice());
			cart.setGrandTotal(cart.getGrandTotal() + cartItem.getProduct().getProductPrice());
		}

		List<CartItem> cartItems = cart.getCartItem();
		cartItems.add(cartItem);
		cart.setCartItem(cartItems);

		Cart updatedCart = this.cartRepository.save(cart);
		return this.modelMapper.map(updatedCart, CartDto.class);
	}

	@Override
	public CartDto removeFromCart(Integer userId, Integer productId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "User Id", userId));
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));

		CartItem cartItem = this.cartItemRepository.findByProductAndCart(product, cart);
		if (cartItem == null) {
			throw new ResourceNotFoundException("CartItem", "ProductId", productId);
		}
		if (cartItem.getQuantity() == 1) {
			cart.setGrandTotal(cart.getGrandTotal() - cartItem.getProduct().getProductPrice());
			this.cartItemRepository.delete(cartItem);
			return this.modelMapper.map(cart, CartDto.class);
		}
		cartItem.setQuantity(cartItem.getQuantity() - 1);
		cartItem.setTotalProductPrice(cartItem.getQuantity() * product.getProductPrice());
		cart.setGrandTotal(cart.getGrandTotal() - cartItem.getProduct().getProductPrice());
		Cart updatedCart = this.cartRepository.save(cart);

		return this.modelMapper.map(updatedCart, CartDto.class);
	}

	@Override
	public CartDto viewCart(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "User Id", userId));

		return this.modelMapper.map(cart, CartDto.class);
	}

	@Override
	public void deleteCart(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "User Id", userId));
		this.cartRepository.delete(cart);
	}

	@Override
	public CartDto removeProductFromCart(Integer userId, Integer productId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User12", "User Id", userId));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart1", "User Id", userId));
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));

		CartItem cartItem = this.cartItemRepository.findByProductAndCart(product, cart);
		if (cartItem == null) {
			throw new ResourceNotFoundException("CartItem", "ProductId", productId);
		}
		cart.setGrandTotal(cart.getGrandTotal() - cartItem.getTotalProductPrice());
		this.cartItemRepository.delete(cartItem);

		Cart updatedCart = this.cartRepository.findById(cart.getCartId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart2", "User Id", userId));

		return this.modelMapper.map(updatedCart, CartDto.class);

	}

}
