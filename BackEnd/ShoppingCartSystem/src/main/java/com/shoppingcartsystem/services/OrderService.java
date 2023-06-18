package com.shoppingcartsystem.services;

import java.util.List;

import com.shoppingcartsystem.payloads.OrderDto;
import com.shoppingcartsystem.payloads.OrderRequest;
import com.shoppingcartsystem.payloads.OrderResponse;
import com.shoppingcartsystem.payloads.TransactionDetails;

public interface OrderService {

	List<OrderDto> createOrder(OrderRequest orderRequest);
	
	OrderDto buySingleProduct(OrderRequest orderRequest);
	
	OrderDto viewOrder(Integer orderId);
	
	OrderResponse viewAllOrderUser(Integer userId, Integer pageNumber, Integer pageSize);
	
	OrderResponse viewAllOrderMerchant(Integer merchantId, Integer pageNumber, Integer pageSize);
	
	OrderResponse viewAllOrders(Integer pageNumber, Integer pageSize);
	
	OrderDto updateOrder(OrderRequest orderRequest, Integer orderId);
	
	OrderDto updateOrderStatus(OrderRequest orderRequest, Integer orderId);
	
	void deleteOrder(Integer orderId);
	
	TransactionDetails createTransaction(Double amount);
}
