package com.shoppingcartsystem.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.shoppingcartsystem.entities.Order;
import com.shoppingcartsystem.repositories.OrderRepository;
import com.shoppingcartsystem.repositories.ProductRepository;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.StatsService;

@Service
public class StatsServiceImpl implements StatsService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Map<String, Object> AdminStats() {
		
		List<Order> orders = this.orderRepository.findAll();
		double turnover = orders.stream().mapToDouble(order->order.getTotalPrice()).sum();
		
		Map<String, Object> stats = new HashMap<>();
		stats.put("totalOrders", this.orderRepository.count());
		stats.put("totalUsers", this.userRepository.findByRole("USER").size());
		stats.put("totalMerchants" ,this.userRepository.findByRole("MERCHANT").size());
		stats.put("totalProducts", this.productRepository.count());
		stats.put("totalTurnover", turnover);
		
		return stats;
	}

	@Override
	public Map<String, Object> MerchantStats(Integer merchantId) {
		Page<Order> myOrders = this.orderRepository.findBySellerId(merchantId, null);
		List<Order> orders = myOrders.getContent();
		double myTurnover = orders.stream().mapToDouble(order->order.getTotalPrice()).sum();
		
		Map<String, Object> stats = new HashMap<>();
		stats.put("myOrders", myOrders.getTotalElements());
		stats.put("myTurnover", myTurnover);
		return stats;
	}

}
