package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adriel.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	List<Order> findByDemo(int demo);
	
}
