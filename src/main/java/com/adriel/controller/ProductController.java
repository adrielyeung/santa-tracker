package com.adriel.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Product;
import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.factory.EntityFactory;
import com.adriel.service.ProductService;
import com.adriel.utils.ConstStrings;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value=ConstStrings.ADD_PRODUCT, method=RequestMethod.GET)
	public String goToNewProductPage(Model model, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		model.addAttribute("product", EntityFactory.getEntity(SantaTrackerEntityType.PRODUCT));
		req.setAttribute("type", "Add");
		return "App/edit-product";
		
	}
	
	@RequestMapping(value=ConstStrings.EDIT_PRODUCT, method=RequestMethod.GET)
	public String goToUpdateProductPage(Model model, HttpServletRequest req, HttpServletResponse resp, @PathVariable String productid) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		
		Product productToUpdate = null;
		try {
			productToUpdate = productService.getProductById(Integer.parseInt(productid));
		} catch (NumberFormatException | ResourceNotFoundException e) {
			Redirections.redirect(req, resp, ConstStrings.PRODUCT, ConstStrings.PRODUCT_ERR, String.format(ConstStrings.READ_ENTITY_ERR, "product with ID" + productid));
			return null;
		}
		model.addAttribute("product", productToUpdate);
		req.setAttribute("type", "Edit");
		return "App/edit-product";
	}
	
	@RequestMapping(value=ConstStrings.PRODUCT, method=RequestMethod.GET)
	public String goToProductPage(HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		List<Product> products = productService.getAllProducts();
		
		req.getSession().setAttribute("products", products);
		
		return "App/product";
		
	}
	
	@RequestMapping(value=ConstStrings.VERIFY_PRODUCT, method=RequestMethod.POST)
	public void verifyProduct(@ModelAttribute("product") Product product, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return;
		}
		
		try {
			Product existingProduct = productService.getProductById(product.getProductID());
			product.setProductID(existingProduct.getProductID());
			product.setProdDets(existingProduct.getProdDets());
			productService.updateProduct(product.getProductID(), product);
			Redirections.redirect(req, resp, ConstStrings.PRODUCT, ConstStrings.PRODUCT_ERR, String.format(ConstStrings.SUCCESS_EDIT_ENTITY, "product"));
		} catch (ResourceNotFoundException e) {
			productService.createProduct(product);
			Redirections.redirect(req, resp, ConstStrings.ADD_PRODUCT, ConstStrings.ADD_PRODUCT_ERR, String.format(ConstStrings.SUCCESS_CREATE_ENTITY, "product"));
		}
	
	}
	
	@RequestMapping(value=ConstStrings.DELETE_PRODUCT, method=RequestMethod.GET)
	public void verifyDeleteProduct(HttpServletRequest req, HttpServletResponse resp, @PathVariable String productid) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
		}
		
		try {
			productService.deleteProduct(Integer.parseInt(productid));
		} catch (NumberFormatException | ResourceNotFoundException e) {
			Redirections.redirect(req, resp, ConstStrings.PRODUCT, ConstStrings.PRODUCT_ERR, String.format(ConstStrings.DELETE_ENTITY_ERR, "product with ID" + productid));
		}
		
		Redirections.redirect(req, resp, ConstStrings.PRODUCT, ConstStrings.PRODUCT_ERR, String.format(ConstStrings.SUCCESS_DELETE_ENTITY, "product"));
	}
	
}
