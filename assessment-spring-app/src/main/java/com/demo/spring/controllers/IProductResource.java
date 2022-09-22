package com.demo.spring.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.demo.spring.entity.Product;
import com.demo.spring.exceptions.ProductExistsException;
import com.demo.spring.exceptions.ProductNotFoundException;
import com.demo.spring.util.Message;

public interface IProductResource {
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity findOneProduct(int productId) throws ProductNotFoundException;
	
	public ResponseEntity<Message> saveProduct(Product product) throws ProductExistsException;
	
	public ResponseEntity<Message> deleteProduct(int productId) throws ProductNotFoundException;
	
	public ResponseEntity<Message> updateProductPrice(int productId, double price)  throws ProductNotFoundException;
	
	public ResponseEntity<Message> updateProduct(Product product) throws ProductNotFoundException ;
	
	public ResponseEntity<List<Product>> listAll();
	
	
}
