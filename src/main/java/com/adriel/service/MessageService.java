package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.MessageRepository;
import com.adriel.repository.OrderRepository;

@Service
public class MessageService {
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private OrderRepository orderRepository;
	
	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}
	
	public Message getMessageById(Integer messageID) throws ResourceNotFoundException {
		return messageRepository.findById(messageID)
				.orElseThrow(() -> new ResourceNotFoundException("Message not found for this id."));
	}
	
	public List<Message> getMessagesByOrderId(Integer orderID) 
			throws ResourceNotFoundException {
		Order order = orderRepository.findById(orderID)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id."));
		
		return messageRepository.findByOrder(order);
	}
	
	public List<Message> getMessagesByPersonId(Integer personID) {
		return messageRepository.findAllMessagesByPersonId(personID);
	}
	
	public Message createMessage(Message message) {
		return messageRepository.save(message);
	}
	
	public Map<String, Boolean> deleteMessage(Integer messageID)
	throws ResourceNotFoundException {
		Message message = messageRepository.findById(messageID)
				.orElseThrow(() -> new ResourceNotFoundException("Message not found for this id."));
		
		messageRepository.delete(message);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
