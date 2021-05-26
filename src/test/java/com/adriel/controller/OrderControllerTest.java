package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.entity.Product;
import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.OrderDetailService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;
import com.adriel.service.ProductService;
import com.adriel.utils.EmailSender;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
	
	@InjectMocks
	OrderController testOrderController;
	
	@Mock
	Model mockMod;
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSess;
	@Mock
	Order mockOrder;
	@Mock
	Person mockPerson;
	@Mock
	OrderService mockOrderService;
	@Mock
	ProductService mockProductService;
	@Mock
	PersonService mockPersonService;
	@Mock
	OrderDetailService mockOrderDetailService;
	@Mock
	EmailSender mockEmailSender;
	@Mock
	List<Order> mockOrderList;
	@Mock
	List<Product> mockProductList;
	@Mock
	List<Person> mockPersonList;
	
	@BeforeEach
	public void init() {
		when(mockReq.getSession()).thenReturn(mockSess);
		when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
	}
	
	@Test
	public void whenGoToNewOrderPageCalled_thenReturnEditOrderString() {
		
		when(mockPerson.getAdmin()).thenReturn(0);
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		
		String ret = testOrderController.goToNewOrderPage(mockMod, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("productList", mockProductList);
		inOrder.verify(mockReq).setAttribute("type", "Add");
		assertEquals("App/edit-order", ret);
	}
	
	@Test
	public void whenGoToOrderPageCalled_thenReturnOrderString() {
		
		when(mockPerson.getPersonOrders()).thenReturn(mockOrderList);
		try {
			when(mockPersonService.getAllCustomers()).thenReturn(mockPersonList);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.goToOrderPage(mockMod, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("customerList", mockPersonList);
		
		assertEquals("App/order", ret);
		
	}
	
	@Test
	public void whenLoadCustomerDataInOrderPageCalled_thenReturnOrderStringWithList() {
		
		Person mockPersonSelected = mock(Person.class);
		when(mockPerson.getPersonID()).thenReturn(1);
		try {
			when(mockPersonService.getPersonById(1)).thenReturn(mockPersonSelected);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			when(mockPersonService.getAllCustomers()).thenReturn(mockPersonList);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.loadCustomerDataInOrderPage(mockPerson, mockMod, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("customerList", mockPersonList);
		
		assertEquals("App/order", ret);
		
	}
	
	@Test
	public void whenGoToUpdateOrderPageCalled_TypeAdd_thenReturnEditOrderString() {
		
		when(mockPerson.getAdmin()).thenReturn(0);
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		
		String submitType = "Add";
		String orderid = "1";
		try {
			when(mockOrderService.getOrderById(Integer.parseInt(orderid))).thenReturn(mockOrder);
		} catch (NumberFormatException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.goToUpdateOrderPage(mockMod, mockReq, mockResp, orderid, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockMod);
		inOrder.verify(mockMod).addAttribute("order", mockOrder);
		inOrder.verify(mockReq).setAttribute("productList", mockProductList);
		inOrder.verify(mockReq).setAttribute("type", submitType);
		assertEquals("App/edit-order", ret);
	}
	
	@Test
	public void whenGoToUpdateOrderPageCalled_TypeEdit_thenReturnEditOrderString() {
		
		when(mockPerson.getAdmin()).thenReturn(0);
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		when(mockOrder.getOrderTime()).thenReturn(LocalDateTime.now());
		
		String submitType = "Edit";
		String orderid = "1";
		try {
			when(mockOrderService.getOrderById(Integer.parseInt(orderid))).thenReturn(mockOrder);
		} catch (NumberFormatException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.goToUpdateOrderPage(mockMod, mockReq, mockResp, orderid, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockMod);
		inOrder.verify(mockMod).addAttribute("order", mockOrder);
		inOrder.verify(mockReq).setAttribute("productList", mockProductList);
		inOrder.verify(mockReq).setAttribute("type", submitType);
		assertEquals("App/edit-order", ret);
	}
	
	@Test
	public void whenGoToUpdateOrderPageCalled_TypeEdit_AfterEditableTime_thenReturnEditOrderString() {
		
		when(mockPerson.getAdmin()).thenReturn(0);
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		// Start of calendar, must be before now
		when(mockOrder.getOrderTime()).thenReturn(LocalDateTime.MIN);
		
		String submitType = "Edit";
		String orderid = "1";
		try {
			when(mockOrderService.getOrderById(Integer.parseInt(orderid))).thenReturn(mockOrder);
		} catch (NumberFormatException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.goToUpdateOrderPage(mockMod, mockReq, mockResp, orderid, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockMod);
		inOrder.verify(mockMod).addAttribute("order", mockOrder);
		inOrder.verify(mockReq).setAttribute("productList", mockProductList);
		inOrder.verify(mockReq).setAttribute("type", submitType);
		assertEquals("App/edit-order", ret);
	}
	
	@Test
	public void whenGoToUpdateOrderPageCalled_TypeSchedule_thenReturnEditOrderString() {
		
		when(mockPerson.getAdmin()).thenReturn(1);
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		
		String submitType = "Schedule";
		String orderid = "1";
		try {
			when(mockOrderService.getOrderById(Integer.parseInt(orderid))).thenReturn(mockOrder);
		} catch (NumberFormatException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testOrderController.goToUpdateOrderPage(mockMod, mockReq, mockResp, orderid, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockMod);
		inOrder.verify(mockMod).addAttribute("order", mockOrder);
		inOrder.verify(mockReq).setAttribute("productList", mockProductList);
		inOrder.verify(mockReq).setAttribute("type", submitType);
		assertEquals("App/edit-order", ret);
	}
	
	@Test
	public void whenVerifyOrderCalled_ExistingOrder_thenUpdateAndRedirectToOrderPage() {
		
		String submitType = "Edit";
		int orderid = 1;
		when(mockOrder.getOrderID()).thenReturn(orderid);
		
		testOrderController.verifyOrder(mockOrder, mockReq, mockResp, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockOrderDetailService);
		try {
			inOrder.verify(mockOrderDetailService).getOrderDetailsByOrderId(orderid);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void whenVerifyOrderCalled_ExistingOrderToSchedule_thenUpdateAndRedirectToOrderPage() {
		
		Order mockExistingOrder = mock(Order.class);
		String submitType = "Schedule";
		int orderid = 1;
		when(mockOrder.getOrderID()).thenReturn(orderid);
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockExistingOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			when(mockOrderService.updateOrder(orderid, mockOrder)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockExistingOrder.getOrderID()).thenReturn(orderid);
		when(mockExistingOrder.getOrderTime()).thenReturn(LocalDateTime.now());
		when(mockExistingOrder.getPerson()).thenReturn(mockPerson);
		when(mockOrder.getPerson()).thenReturn(mockPerson);
		when(mockOrder.getOrderTime()).thenReturn(LocalDateTime.now());
		when(mockOrder.getEstimatedTime()).thenReturn(LocalDateTime.now());
		when(mockOrder.getPlannedTime()).thenReturn(LocalDateTime.now());
		
		StringBuffer sb = new StringBuffer();
		sb.append("localhost:8080/index");
		when(mockReq.getRequestURL()).thenReturn(sb);
		when(mockReq.getServletPath()).thenReturn("localhost:8080");
		
		testOrderController.verifyOrder(mockOrder, mockReq, mockResp, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockOrderService);
		try {
			inOrder.verify(mockOrderService).updateOrder(orderid, mockOrder);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void whenVerifyOrderCalled_NewOrder_thenCreateAndRedirectToEditOrderPage() {
		
		Order mockOrderSaved = mock(Order.class);
		String submitType = "Add";
		List<OrderDetail> orderDetsTest = new ArrayList<>();
		orderDetsTest.add(new OrderDetail(1, new Product(), mockOrder));
		when(mockOrder.getOrderDets()).thenReturn(orderDetsTest);
		when(mockOrderSaved.getOrderID()).thenReturn(1);
		when(mockOrderService.createOrder(mockOrder)).thenReturn(mockOrderSaved);
		when(mockOrderSaved.getPerson()).thenReturn(mockPerson);
		when(mockOrderSaved.getOrderTime()).thenReturn(LocalDateTime.now());
		
		StringBuffer sb = new StringBuffer();
		sb.append("localhost:8080/index");
		when(mockReq.getRequestURL()).thenReturn(sb);
		when(mockReq.getServletPath()).thenReturn("localhost:8080");
		
		testOrderController.verifyOrder(mockOrder, mockReq, mockResp, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockOrderService);
		inOrder.verify(mockOrderService).createOrder(mockOrder);
	}
	
	@Test
	public void whenVerifyOrderCalled_NewOrder_NoNewItems_thenCreateAndRedirectToEditOrderPage() {
		
		String submitType = "Add";
		
		List<OrderDetail> orderDetsTest = new ArrayList<>();
		when(mockOrder.getOrderDets()).thenReturn(orderDetsTest);
		
		testOrderController.verifyOrder(mockOrder, mockReq, mockResp, submitType);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockOrderService);
		inOrder.verify(mockOrderService, never()).createOrder(mockOrder);
	}
	
//	@Test
//	public void whenVerifyDeleteOrderCalled_thenDeleteAndRedirectToOrderPage() {
//		
//		String orderid = "1";
//		
//		testOrderController.verifyDeleteOrder(mockReq, mockResp, orderid);
//		
//		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockOrderService);
//		try {
//			inOrder.verify(mockOrderService).deleteOrder(Integer.parseInt(orderid));
//		} catch (ResourceNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
}
