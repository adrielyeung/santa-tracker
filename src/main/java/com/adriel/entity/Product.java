package com.adriel.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "prodDets"})
public class Product extends SantaTrackerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private int productID;
	
	@Size(min = 1, max = 50)
	private String name;
	@Size(max = 100)
	private String spec;
	private double unitPrice;
	
	@OneToMany(mappedBy = "product")
	private List<OrderDetail> prodDets = new ArrayList<>();
	
	public Product() {
		
	}

	public Product(String name, String spec, double unitPrice) {
		super();
		this.name = name;
		this.spec = spec;
		this.unitPrice = unitPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public List<OrderDetail> getProdDets() {
		return prodDets;
	}

	public void setProdDets(List<OrderDetail> prodDets) {
		this.prodDets = prodDets;
	}
	
	public void addOrderDetail(OrderDetail ordd) {
		if (! prodDets.contains(ordd) ) {
			this.prodDets.add(ordd);
			ordd.setProduct(this);
		}
	}

	@Override
	public String toString() {
		return "Product [productID=" + productID + ", name=" + name + ", spec=" + spec + ", unitPrice=" + unitPrice
				+ "]";
	}
}
