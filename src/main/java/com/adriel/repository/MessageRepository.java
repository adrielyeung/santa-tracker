package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adriel.entity.Message;
import com.adriel.entity.Order;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	List<Message> findByOrder(Order order);
}
