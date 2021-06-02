package com.adriel.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "orderMsgs", "orderDets"})
public class Order extends SantaTrackerEntity implements Comparable<Order> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private int orderID;
	
	private LocalDateTime orderTime;
	private LocalDateTime plannedTime;
	private LocalDateTime estimatedTime;
	private int status;
	private String location;
	private int demo;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "personID", referencedColumnName = "personID")
	private Person person;

	@OneToMany(mappedBy = "order")
	private List<Message> orderMsgs = new ArrayList<>();
	
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDets = new ArrayList<>();
	
	public Order() {
		
	}

	public Order(Person person, LocalDateTime orderTime, LocalDateTime plannedTime, LocalDateTime estimatedTime,
			int status, String location) {
		super();
		this.person = person;
		this.orderTime = orderTime;
		this.plannedTime = plannedTime;
		this.estimatedTime = estimatedTime;
		this.status = status;
		this.location = location;
	}

	public int getPersonID() {
		return this.person.getPersonID();
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}

	public LocalDateTime getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(LocalDateTime estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getDemo() {
		return demo;
	}

	public void setDemo(int demo) {
		this.demo = demo;
	}

	public int getOrderID() {
		return orderID;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public List<Message> getOrderMsgs() {
		return orderMsgs;
	}

	public void setOrderMsgs(List<Message> orderMsgs) {
		this.orderMsgs = orderMsgs;
	}

	public List<OrderDetail> getOrderDets() {
		return orderDets;
	}

	public void setOrderDets(List<OrderDetail> orderDets) {
		this.orderDets = orderDets;
	}
	
	public void addMessage(Message msg) {
		this.orderMsgs.add(msg);
		msg.setOrder(this);
	}

	public void addOrderDetail(OrderDetail ordd) {
		this.orderDets.add(ordd);
		ordd.setOrder(this);
	}
	
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", PersonID=" + this.getPersonID() + ", orderTime=" + orderTime
				+ ", plannedTime=" + plannedTime + ", estimatedTime=" + estimatedTime + ", status=" + status
				+ ", location=" + location + "]";
	}
	
	/**
	 * Compares to other <tt>Order</tt> objects by date (OrderTime), closer date first (smaller).
	 */
	@Override
	public int compareTo(Order o) {
		if (this.getOrderTime().isAfter(o.getOrderTime())) {
			return -1;
		} else if (this.getOrderTime().isEqual(o.getOrderTime())) {
			return 0;
		} else {
			return 1;
		}
	}
}
