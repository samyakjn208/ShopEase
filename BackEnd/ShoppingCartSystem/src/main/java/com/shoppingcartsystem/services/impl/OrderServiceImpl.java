package com.shoppingcartsystem.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shoppingcartsystem.entities.Cart;
import com.shoppingcartsystem.entities.CartItem;
import com.shoppingcartsystem.entities.DeliveryDetails;
import com.shoppingcartsystem.entities.Order;
import com.shoppingcartsystem.entities.Product;
import com.shoppingcartsystem.entities.User;
import com.shoppingcartsystem.exceptions.OutOfStockException;
import com.shoppingcartsystem.exceptions.ResourceNotFoundException;
import com.shoppingcartsystem.payloads.OrderDto;
import com.shoppingcartsystem.payloads.OrderRequest;
import com.shoppingcartsystem.payloads.OrderResponse;
import com.shoppingcartsystem.payloads.TransactionDetails;
import com.shoppingcartsystem.repositories.CartRepository;
import com.shoppingcartsystem.repositories.DeliveryDetailsRepository;
import com.shoppingcartsystem.repositories.OrderRepository;
import com.shoppingcartsystem.repositories.ProductRepository;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DeliveryDetailsRepository deliveryDetailsRepository;

	@Autowired
	private ModelMapper modelMapper;

	//Removed key and key_secret since they are confidential
	private static final String KEY = "";
	private static final String KEY_SECRET = "";
	private static final String CURRENCY = "INR";

	@Override
	public List<OrderDto> createOrder(OrderRequest orderRequest) {
		User user = this.userRepository.findById(orderRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", orderRequest.getUserId()));

		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "Cart Id", orderRequest.getCartId()));

		DeliveryDetails deliveryDetails = this.deliveryDetailsRepository.findById(orderRequest.getDeliveryDetailsId())
				.orElseThrow(() -> new ResourceNotFoundException("DeliveryDetails", "Id",
						orderRequest.getDeliveryDetailsId()));

		List<CartItem> cartItems = cart.getCartItem();

		List<Order> orders = cartItems.stream().map(cartItem -> {
			Product product = cartItem.getProduct();

			if (product.getStock() < cartItem.getQuantity()) {
				throw new OutOfStockException();
			}

			User merchant = this.userRepository.findById(cartItem.getProduct().getMerchantId())
					.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", orderRequest.getUserId()));
			Order order = new Order();
			order.setOrderId(orderIdGenerator(user.getUserId()));
			order.setOrderDate(LocalDate.now());
			order.setShippedTo(deliveryDetails.getName());
			order.setShippingAddress(deliveryDetails.getAddressLine1() + ", " + deliveryDetails.getAddressLine2() + ", "
					+ deliveryDetails.getCity() + ", " + deliveryDetails.getState() + " - "
					+ deliveryDetails.getPincode());
			order.setPaymentMethod(orderRequest.getPaymentMethod());
			order.setPaymentDetails(orderRequest.getPaymentDetails());
			order.setProduct(cartItem.getProduct());
			order.setQuantity(cartItem.getQuantity());
			order.setPrice(cartItem.getProduct().getProductPrice());
			order.setTotalPrice(cartItem.getTotalProductPrice());
			order.setSellerId(merchant.getUserId());
			order.setSellerName(merchant.getName());
			if (orderRequest.getPaymentMethod().equals("COD")) {
				order.setOrderStatus("Order Created");
				order.setPaymentDetails("COD");
			} else {
				order.setOrderStatus("Payment Pending");
				order.setPaymentMethod("Pre-Paid");
			}
			// Decreasing stock quantity
			product.setStock(product.getStock() - cartItem.getQuantity());
			this.productRepository.save(product);

			order.setUser(user);
			this.orderRepository.save(order);

			return order;
		}).collect(Collectors.toList());

		List<OrderDto> orderDtos = orders.stream().map((order) -> this.modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		this.cartRepository.delete(cart);

		return orderDtos;

	}

	@Override
	public OrderDto buySingleProduct(OrderRequest orderRequest) {
		User user = this.userRepository.findById(orderRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", orderRequest.getUserId()));
		DeliveryDetails deliveryDetails = this.deliveryDetailsRepository.findById(orderRequest.getDeliveryDetailsId())
				.orElseThrow(() -> new ResourceNotFoundException("DeliveryDetails", "Id",
						orderRequest.getDeliveryDetailsId()));
		Product product = this.productRepository.findById(orderRequest.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", orderRequest.getProductId()));
		User merchant = this.userRepository.findById(product.getMerchantId()).orElse(new User());
		Order order = new Order();

		if (product.getStock() == 0) {
			throw new OutOfStockException();
		}

		order.setOrderId(orderIdGenerator(user.getUserId()));
		order.setOrderDate(LocalDate.now());
		order.setShippedTo(deliveryDetails.getName());
		order.setShippingAddress(deliveryDetails.getAddressLine1() + ", " + deliveryDetails.getAddressLine2() + ", "
				+ deliveryDetails.getCity() + ", " + deliveryDetails.getState() + " - " + deliveryDetails.getPincode());
		order.setPaymentMethod(orderRequest.getPaymentMethod());
		order.setPaymentDetails(orderRequest.getPaymentDetails());
		order.setProduct(product);
		order.setQuantity(1);
		order.setPrice(product.getProductPrice());
		order.setTotalPrice(product.getProductPrice());
		order.setSellerId(merchant.getUserId());
		order.setSellerName(merchant.getName());
		if (orderRequest.getPaymentMethod().equals("COD")) {
			order.setOrderStatus("Order Created");
			order.setPaymentDetails("COD");
		} else {
			order.setOrderStatus("Payment Pending");
			order.setPaymentMethod("Pre-Paid");
		}
		// Decreasing stock quantity
		product.setStock(product.getStock() - 1);
		this.productRepository.save(product);

		order.setUser(user);
		Order savedOrder = this.orderRepository.save(order);

		return this.modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public OrderDto viewOrder(Integer orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Order Id", orderId));
		;
		return this.modelMapper.map(order, OrderDto.class);
	}

	@Override
	public OrderResponse viewAllOrderUser(Integer userId, Integer pageNumber, Integer pageSize) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		Sort sort = Sort.by("id").descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> orderPage = this.orderRepository.findByUser(user, p);
		List<Order> orders = orderPage.getContent();
		List<OrderDto> orderDtos = orders.stream().map((order) -> this.modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setContent(orderDtos);
		orderResponse.setPageNumber(0);
		orderResponse.setPageSize(orderPage.getSize());
		orderResponse.setTotalElements(orderPage.getTotalElements());
		orderResponse.setTotalPages(orderPage.getTotalPages());
		orderResponse.setLastPage(orderPage.isLast());

		return orderResponse;
	}

	@Override
	public OrderResponse viewAllOrderMerchant(Integer merchantId, Integer pageNumber, Integer pageSize) {
		Sort sort = Sort.by("id").descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Order> orderPage = this.orderRepository.findBySellerId(merchantId, p);
		List<Order> orders = orderPage.getContent();
		List<OrderDto> orderDtos = orders.stream().map((order) -> this.modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setContent(orderDtos);
		orderResponse.setPageNumber(0);
		orderResponse.setPageSize(orderPage.getSize());
		orderResponse.setTotalElements(orderPage.getTotalElements());
		orderResponse.setTotalPages(orderPage.getTotalPages());
		orderResponse.setLastPage(orderPage.isLast());

		return orderResponse;
	}

	@Override
	public OrderResponse viewAllOrders(Integer pageNumber, Integer pageSize) {

		Sort sort = Sort.by("id").descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Order> orderPage = this.orderRepository.findAll(p);
		List<Order> orders = orderPage.getContent();
		List<OrderDto> orderDtos = orders.stream().map((order) -> this.modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setContent(orderDtos);
		orderResponse.setPageNumber(0);
		orderResponse.setPageSize(orderPage.getSize());
		orderResponse.setTotalElements(orderPage.getTotalElements());
		orderResponse.setTotalPages(orderPage.getTotalPages());
		orderResponse.setLastPage(orderPage.isLast());

		return orderResponse;
	}

	@Override
	public OrderDto updateOrder(OrderRequest orderRequest, Integer orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Order Id", orderId));
		order.setOrderStatus(orderRequest.getOrderStatus());
		order.setPaymentDetails(orderRequest.getPaymentDetails());
		Order updatedOrder = this.orderRepository.save(order);
		return this.modelMapper.map(updatedOrder, OrderDto.class);
	}

	@Override
	public OrderDto updateOrderStatus(OrderRequest orderRequest, Integer orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Order Id", orderId));
		order.setOrderStatus(orderRequest.getOrderStatus());
		Order updatedOrder = this.orderRepository.save(order);
		return this.modelMapper.map(updatedOrder, OrderDto.class);
	}

	@Override
	public void deleteOrder(Integer orderId) {
		Order order = this.orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "Order Id", orderId));
		this.orderRepository.delete(order);
	}

	private String orderIdGenerator(int userId) {
		int min = 1000;
		int max = 9999;
		int random = (int) (Math.random() * (max - min + 1) + min);

		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
		String dateString = date.format(formatter);

		String orderId = userId + dateString.substring(0, 2) + "-" + dateString.substring(2) + random;
		return orderId;
	}

	@Override
	public TransactionDetails createTransaction(Double amount) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", (amount * 100));
			jsonObject.put("currency", CURRENCY);

			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
			com.razorpay.Order order = razorpayClient.orders.create(jsonObject);

			return prepareTransactionDetails(order);
		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		return null;
	}

	private TransactionDetails prepareTransactionDetails(com.razorpay.Order order) {
		String orderId = order.get("id");
		String currency = order.get("currency");
		Integer amount = order.get("amount");

		TransactionDetails transactionDetails = new TransactionDetails(orderId, currency, amount, KEY);
		return transactionDetails;
	}
}
