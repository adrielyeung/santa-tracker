package com.adriel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "OrderDetail")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderDetail extends SantaTrackerEntity implements Comparable<OrderDetail> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private int orderDetID;
	
	private int quantity;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "productID", referencedColumnName = "productID")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "orderID", referencedColumnName = "orderID")
	private Order order;
	
	public OrderDetail() {
		
	}
	
	public OrderDetail(int quantity, Product product, Order order) {
		super();
		this.quantity = quantity;
		this.product = product;
		this.order = order;
		this.product.addOrderDetail(this);
		this.order.addOrderDetail(this);
	}

	public int getOrderDetID() {
		return orderDetID;
	}

	public void setOrderDetID(int orderDetID) {
		this.orderDetID = orderDetID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	/**
	 * Compares to other <tt>OrderDetail</tt> objects by ProductID.
	 */
	@Override
	public int compareTo(OrderDetail o) {
		if (this.getProduct().getProductID() < o.getProduct().getProductID()) {
			return -1;
		} else if (this.getProduct().getProductID() == o.getProduct().getProductID()) {
			return 0;
		} else {
			return 1;
		}
	}
}
