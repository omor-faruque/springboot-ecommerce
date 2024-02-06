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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pwm.springbootecommerce.dto.CategoryDto;
import com.pwm.springbootecommerce.model.Category;
import com.pwm.springbootecommerce.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {

	private CategoryService categoryService;
	
	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/categories-with-products")
	public ResponseEntity<List<Category>> getAllCategoriesWithProducts() {
		List<Category> categories = this.categoryService.getCategoriesWithProducts();
		if (categories.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(categories, HttpStatus.OK);

	}
	
	@GetMapping("/public/categories")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		List<CategoryDto> categories = this.categoryService.getCategories();
		if (categories.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(categories, HttpStatus.OK);

	}
	
	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id)  {
		Category category = this.categoryService.getCategoryById(id);
		return new ResponseEntity<Category>(category,HttpStatus.OK);
	}
	
	@PostMapping("/categories")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) throws Exception {
		return new ResponseEntity<Category>(categoryService.addCategory(category), HttpStatus.CREATED);
	}
	

	
	@DeleteMapping("/categories/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteCategoryById(@PathVariable Long id) {
		this.categoryService.deleteCategoryById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
