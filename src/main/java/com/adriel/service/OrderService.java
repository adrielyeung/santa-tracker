package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.adriel.entity.Order;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
	public Order getOrderById(Integer orderID) throws ResourceNotFoundException {
		return orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
	}
	
	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}
	
	public ResponseEntity<Order> updateOrder(int orderID, Order orderDetails) throws ResourceNotFoundException {
		Order order = orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
		
		order.setOrderTime(orderDetails.getOrderTime());
		order.setPlannedTime(orderDetails.getPlannedTime());
		order.setEstimatedTime(orderDetails.getEstimatedTime());
		order.setStatus(orderDetails.getStatus());
		order.setLocation(orderDetails.getLocation());
		final Order updatedOrder = orderRepository.save(order);
		return ResponseEntity.ok(updatedOrder);
	}
	
	public Map<String, Boolean> deleteOrder(Integer orderID) throws ResourceNotFoundException {
		Order order = orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
		
		orderRepository.delete(order);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
