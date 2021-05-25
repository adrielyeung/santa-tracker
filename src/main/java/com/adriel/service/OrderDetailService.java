package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.OrderDetailRepository;
import com.adriel.repository.OrderRepository;

@Service
public class OrderDetailService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	public List<OrderDetail> getAllOrderDetails() {
		return orderDetailRepository.findAll();
	}
	
	public OrderDetail getOrderDetailById(Integer orderDetailID) throws ResourceNotFoundException {
		return orderDetailRepository.findById(orderDetailID)
				.orElseThrow(() -> new ResourceNotFoundException("Order detail not found for this id."));
	}
	
	public List<OrderDetail> getOrderDetailsByOrderId(Integer orderID) 
			throws ResourceNotFoundException {
		Order order = orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
		
		return orderDetailRepository.findByOrder(order);
	}
	
	public OrderDetail createOrderDetail(OrderDetail orderDetail) {
		return orderDetailRepository.save(orderDetail);
	}
	
	public ResponseEntity<OrderDetail> updateOrderDetail(int orderDetailID, OrderDetail orderDetailInput) throws ResourceNotFoundException {
		OrderDetail orderDetail = orderDetailRepository.findById(orderDetailID)
				.orElseThrow(() -> new ResourceNotFoundException("Order detail not found for this id."));
		
		orderDetail.setProduct(orderDetailInput.getProduct());
		orderDetail.setQuantity(orderDetailInput.getQuantity());
		final OrderDetail updatedOrder = orderDetailRepository.save(orderDetail);
		return ResponseEntity.ok(updatedOrder);
	}
	
	public Map<String, Boolean> deleteOrderDetail(Integer orderDetailID) throws ResourceNotFoundException {
		OrderDetail order = orderDetailRepository.findById(orderDetailID)
				.orElseThrow(() -> new ResourceNotFoundException("Order detail not found for this id."));
		
		orderDetailRepository.delete(order);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
