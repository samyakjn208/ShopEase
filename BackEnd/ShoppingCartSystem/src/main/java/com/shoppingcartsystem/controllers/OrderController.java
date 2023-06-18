package com.shoppingcartsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcartsystem.config.AppConstants;
import com.shoppingcartsystem.payloads.OrderDto;
import com.shoppingcartsystem.payloads.OrderRequest;
import com.shoppingcartsystem.payloads.OrderResponse;
import com.shoppingcartsystem.payloads.TransactionDetails;
import com.shoppingcartsystem.services.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/")
	public ResponseEntity<List<OrderDto>> createOrder(@RequestBody OrderRequest orderRequest){
		List<OrderDto> savedOrder = this.orderService.createOrder(orderRequest);
		return new ResponseEntity<List<OrderDto>>(savedOrder, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/buyNow")
	public ResponseEntity<OrderDto> buySingleProduct(@RequestBody OrderRequest orderRequest){
		OrderDto savedOrder = this.orderService.buySingleProduct(orderRequest);
		return new ResponseEntity<OrderDto>(savedOrder, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT','USER')")
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> viewOrder(@PathVariable Integer orderId){
		OrderDto order = this.orderService.viewOrder(orderId);
		return new ResponseEntity<OrderDto>(order, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT','USER')")
	@GetMapping("/userOrders/{userId}")
	public ResponseEntity<OrderResponse> viewAllOrderUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required= false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
			){
		OrderResponse order = this.orderService.viewAllOrderUser(userId, pageNumber, pageSize);
		return new ResponseEntity<OrderResponse>(order, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT')")
	@GetMapping("/merchantOrders/{merchantId}")
	public ResponseEntity<OrderResponse> viewAllOrderMerchant(@PathVariable Integer merchantId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required= false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize){
		OrderResponse order = this.orderService.viewAllOrderMerchant(merchantId, pageNumber, pageSize);
		return new ResponseEntity<OrderResponse>(order, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<OrderResponse> viewAllOrder(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required= false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize			){
		OrderResponse order = this.orderService.viewAllOrders(pageNumber, pageSize);
		return new ResponseEntity<OrderResponse>(order, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT')")
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderRequest orderRequest, @PathVariable Integer orderId){
		OrderDto updatedOrder = this.orderService.updateOrder(orderRequest, orderId);
		return new ResponseEntity<OrderDto>(updatedOrder, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT','USER')")
	@PutMapping("/orderStatus/{orderId}")
	public ResponseEntity<OrderDto> updateOrderStatus(@RequestBody OrderRequest orderRequest, @PathVariable Integer orderId){
		OrderDto updatedOrder = this.orderService.updateOrderStatus(orderRequest, orderId);
		return new ResponseEntity<OrderDto>(updatedOrder, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@GetMapping("/createTransaction/{amount}")
	public TransactionDetails createTransaction(@PathVariable Double amount) {
		return this.orderService.createTransaction(amount);		
	}
}
