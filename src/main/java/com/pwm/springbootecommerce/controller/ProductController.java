package com.pwm.springbootecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pwm.springbootecommerce.dto.ProductWithCategoryDTO;
import com.pwm.springbootecommerce.model.Product;
import com.pwm.springbootecommerce.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products-without-category")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = this.productService.getProducts();
		if (products.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/public/products")
	public ResponseEntity<List<ProductWithCategoryDTO>> getAllProductsWithCategory() {
		List<ProductWithCategoryDTO> products = this.productService.getProductsWithCategory();
		if (products.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(products, HttpStatus.OK);

	}

	@PostMapping("/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ProductWithCategoryDTO> addProduct(@RequestBody ProductWithCategoryDTO productDto) {
		return new ResponseEntity<ProductWithCategoryDTO>(productService.addProduct(productDto), HttpStatus.CREATED);
	}


	@GetMapping("/public/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = this.productService.getProductById(id);
		return new ResponseEntity<Product>(product, HttpStatus.OK);

	}
	
	@PutMapping("/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ProductWithCategoryDTO> updateProduct(@RequestBody ProductWithCategoryDTO productDto) {
		return new ResponseEntity<ProductWithCategoryDTO>(this.productService.updateProduct(productDto), HttpStatus.OK);

	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Long id) {
		this.productService.deleteProductById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
