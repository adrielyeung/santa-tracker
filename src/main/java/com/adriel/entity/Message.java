package com.adriel.entity;

import java.time.LocalDateTime;

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
@Table(name = "Message")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Message extends SantaTrackerEntity implements Comparable<Message> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private int messageID;
	
	private String title;
	private String message;
	private int fromCustomer;
	private LocalDateTime sentTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderID", nullable = false)
	private Order order;
	
	public Message() {
		
	}

	public Message(String title, String message, int fromCustomer, LocalDateTime sentTime, Order order) {
		super();
		this.title = title;
		this.message = message;
		this.fromCustomer = fromCustomer;
		this.sentTime = sentTime;
		this.order = order;
		this.order.addMessage(this);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFromCustomer() {
		return fromCustomer;
	}

	public void setFromCustomer(int fromCustomer) {
		this.fromCustomer = fromCustomer;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public int getOrderID() {
		return this.order.getOrderID();
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public LocalDateTime getSentTime() {
		return sentTime;
	}

	public void setSentTime(LocalDateTime sentTime) {
		this.sentTime = sentTime;
	}

	@Override
	public String toString() {
		return "Message [messageID=" + messageID + ", title=" + title + ", message=" + message + ", isfromCustomer="
				+ fromCustomer + ", order=" + order + "]";
	}
	
	/**
	 * Compares to other <tt>Message</tt> objects by SentTime (latest first).
	 */
	@Override
	public int compareTo(Message o) {
		if (this.getSentTime().isAfter(o.getSentTime())) {
			return -1;
		} else if (this.getSentTime().isEqual(o.getSentTime())) {
			return 0;
		} else {
			return 1;
		}
	}
	
}
