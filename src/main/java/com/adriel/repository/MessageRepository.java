package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adriel.entity.Message;
import com.adriel.entity.Order;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	List<Message> findByOrder(Order order);
	
	@Query("select m from Message m "
			+ "inner join Order o "
			+ "with m.order = o "
			+ "inner join Person p "
			+ "with o.person = p "
			+ "where p.personID = :personId")
	List<Message> findAllMessagesByPersonId(@Param("personId") int personId);
}
