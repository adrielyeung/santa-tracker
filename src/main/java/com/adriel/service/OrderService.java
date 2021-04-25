package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.adriel.entity.Person;
import com.adriel.entity.Order;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.PersonRepository;
import com.adriel.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private PersonRepository customerRepository;
	
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
	public Order getOrderById(@PathVariable(value = "id") Integer orderID) throws ResourceNotFoundException {
		return orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
	}
	
	public Order createOrder(@Valid @RequestBody Order order) throws ResourceNotFoundException {
		// Add this order to the customer before saving
		Person customer = customerRepository.findById(order.getPersonID())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id."));
		customer.addOrder(order);
		customerRepository.save(customer);
		return orderRepository.save(order);
	}
	
	public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") int orderID,
			@Valid @RequestBody Order orderDetails) throws ResourceNotFoundException {
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
	
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Integer orderID)
	throws ResourceNotFoundException {
		Order order = orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
		
		orderRepository.delete(order);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
