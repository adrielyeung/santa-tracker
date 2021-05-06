package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.adriel.entity.Product;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	public Product getProductById(Integer productID) throws ResourceNotFoundException {
		return productRepository.findById(productID)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id."));
	}
	
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}
	
	public ResponseEntity<Product> updateProduct(int productID, Product productDetails) throws ResourceNotFoundException {
		Product product = productRepository.findById(productID)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id."));
		
		product.setName(productDetails.getName());
		product.setSpec(productDetails.getSpec());
		product.setUnitPrice(productDetails.getUnitPrice());
		final Product updatedProduct = productRepository.save(product);
		return ResponseEntity.ok(updatedProduct);
	}
	
	public Map<String, Boolean> deleteProduct(Integer productID) throws ResourceNotFoundException {
		Product product = productRepository.findById(productID)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id."));
		
		productRepository.delete(product);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
