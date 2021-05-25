package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.entity.Product;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.ProductService;
import com.adriel.utils.EmailSender;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	
	@InjectMocks
	ProductController testProductController;
	
	@Mock
	Model mockMod;
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSess;
	@Mock
	Product mockProduct;
	@Mock
	Person mockPerson;
	@Mock
	ProductService mockProductService;
	@Mock
	EmailSender mockEmailSender;
	@Mock
	List<Product> mockProductList;
	
	@BeforeEach
	public void init() {
		when(mockReq.getSession()).thenReturn(mockSess);
		when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
		
	}
	
	@Test
	public void whenGoToNewProductPageCalled_thenReturnEditProductString() {
		
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testProductController.goToNewProductPage(mockMod, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("type", "Add");
		assertEquals("App/edit-product", ret);
	}
	
	@Test
	public void whenGoToUpdateProductPageCalled_thenReturnEditProductString() {
		
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String productid = "1";
		try {
			when(mockProductService.getProductById(Integer.parseInt(productid))).thenReturn(mockProduct);
		} catch (NumberFormatException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		String ret = testProductController.goToUpdateProductPage(mockMod, mockReq, mockResp, productid);
		
		InOrder inOrder = inOrder(mockReq, mockMod);
		inOrder.verify(mockMod).addAttribute("product", mockProduct);
		inOrder.verify(mockReq).setAttribute("type", "Edit");
		assertEquals("App/edit-product", ret);
	}
	
	@Test
	public void whenGoToProductPageCalled_thenReturnProductString() {
		
		when(mockProductService.getAllProducts()).thenReturn(mockProductList);
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testProductController.goToProductPage(mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockSess).setAttribute("products", mockProductList);
		assertEquals("App/product", ret);
	}
	
	@Test
	public void whenVerifyProductCalled_ExistingProduct_thenUpdateAndRedirectToProductPage() {
		
		Product mockExistingProduct = mock(Product.class);
		int productid = 1;
		when(mockProduct.getProductID()).thenReturn(productid);
		try {
			when(mockProductService.getProductById(productid)).thenReturn(mockExistingProduct);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		
		testProductController.verifyProduct(mockProduct, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockProductService);
		try {
			inOrder.verify(mockProductService).getProductById(productid);
			inOrder.verify(mockProductService).updateProduct(productid, mockProduct);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void whenVerifyProductCalled_NewProduct_thenCreateAndRedirectToAddProductPage() {
		
		int productid = 1;
		when(mockProduct.getProductID()).thenReturn(productid);
		try {
			when(mockProductService.getProductById(productid)).thenThrow(ResourceNotFoundException.class);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		
		testProductController.verifyProduct(mockProduct, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockProductService);
		try {
			inOrder.verify(mockProductService).getProductById(productid);
			inOrder.verify(mockProductService).createProduct(mockProduct);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
//	@Test
//	public void whenVerifyDeleteProductCalled_thenDeleteAndRedirectToProductPage() {
//		
//		String productid = "1";
//		
//		testProductController.verifyDeleteProduct(mockReq, mockResp, productid);
//		
//		InOrder inOrder = inOrder(mockReq, mockResp, mockSess, mockProductService);
//		try {
//			inOrder.verify(mockProductService).deleteProduct(Integer.parseInt(productid));
//		} catch (ResourceNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
}
