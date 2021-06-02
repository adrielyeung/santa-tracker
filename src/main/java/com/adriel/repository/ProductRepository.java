package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adriel.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findByDemo(int demo);
	
}
