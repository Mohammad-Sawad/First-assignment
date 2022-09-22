package com.demo.spring.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.spring.entity.Product;
import com.demo.spring.exceptions.ProductExistsException;
import com.demo.spring.exceptions.ProductNotFoundException;
import com.demo.spring.repository.ProductRepository;
import com.demo.spring.util.Message;

@RestController
@RequestMapping("/products")
public class ProductRestController implements IProductResource {

	@Autowired
	ProductRepository productRepository;

	@SuppressWarnings("rawtypes")
	@Override
	@GetMapping(path = "/find/{pid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity findOneProduct(@PathVariable("pid") int productId) throws ProductNotFoundException {
		Optional<Product> productOp = productRepository.findById(productId);
		if (productOp.isPresent()) {
			return ResponseEntity.ok(productOp.get());
		} else {
			throw new ProductNotFoundException();
		}
	}

	@Override
	@PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> saveProduct(@RequestBody Product product) throws ProductExistsException {
		if (productRepository.existsById(product.getProductId())) {
			throw new ProductExistsException();
		} else {
			productRepository.save(product);
			return ResponseEntity.ok(new Message("product saved"));
		}
	}

	@Override
	@DeleteMapping(path = "/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> deleteProduct(@PathVariable("id") int productId) throws ProductNotFoundException {
		if (productRepository.existsById(productId)) {
			productRepository.deleteById(productId);
			return ResponseEntity.ok(new Message("product deleted"));
		} else {
			throw new ProductNotFoundException();
		}
	}

	@Override
	@PatchMapping(path = "/update/{productId}/{newPrice}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateProductPrice(@PathVariable("productId") int productId, @PathVariable("newPrice") double price) throws ProductNotFoundException {
		Optional<Product> prodOp = productRepository.findById(productId);
		if (prodOp.isPresent()) {
			productRepository.updatePrice(productId, price);
			return ResponseEntity.ok(new Message("price updated"));
		} else {
			throw new ProductNotFoundException();
		}
	}

	@Override
	@PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateProduct(@RequestBody Product product) throws ProductNotFoundException {
		if (productRepository.existsById(product.getProductId())) {
			productRepository.save(product);
			return ResponseEntity.ok(new Message("product updated"));
		} else {
			throw new ProductNotFoundException();
		}
	}

	@Override
	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<Product>> listAll() {
		       return ResponseEntity.ok(productRepository.findAll());
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Message> handleProductNotFound(ProductNotFoundException ex) {
		return ResponseEntity.status(404).body(new Message("product not found"));
	}

	@ExceptionHandler(ProductExistsException.class)
	public ResponseEntity<Message> handleProductExists(ProductExistsException ex) {
		return ResponseEntity.ok(new Message("product exists"));
	}

}
