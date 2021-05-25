package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	List<OrderDetail> findByOrder(Order order);
}
