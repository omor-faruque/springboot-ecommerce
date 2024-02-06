package com.pwm.springbootecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pwm.springbootecommerce.dto.ProductWithCategoryDTO;
import com.pwm.springbootecommerce.exception.CategoryNotFoundException;
import com.pwm.springbootecommerce.exception.ProductNotFoundException;
import com.pwm.springbootecommerce.model.Category;
import com.pwm.springbootecommerce.model.Product;
import com.pwm.springbootecommerce.repository.CategoryRepository;
import com.pwm.springbootecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Product> getProducts() {
		return productRepository.findAll();
	}
	
	public List<ProductWithCategoryDTO> getProductsWithCategory() {
		return productRepository.findAllProductsWithCategory();
	}

	public ProductWithCategoryDTO addProduct(ProductWithCategoryDTO productDto) {

		Product _product = new Product();
		_product.setName(productDto.getName());
		_product.setDescription(productDto.getDescription());
		_product.setImage(productDto.getImage());
		_product.setPrice(productDto.getPrice());

		if (productDto.getCategoryName() == null || productDto.getCategoryName().isBlank()) {
			throw new IllegalArgumentException("Product Category missing!");
		} else if (!categoryRepository.existsByName(productDto.getCategoryName())) {
			throw new CategoryNotFoundException("Category dont exist");
		}

		Category _category = categoryRepository.findByName(productDto.getCategoryName());
		_product.setCategory(_category);
		
		Product savedProduct = this.productRepository.save(_product);

		return new ProductWithCategoryDTO(savedProduct);
	}


	public Product getProductById(Long id) {
		Optional<Product> prOptional = this.productRepository.findById(id);
		if (prOptional.isPresent()) {
			return prOptional.get();
		} else {
			throw new ProductNotFoundException("Product with id: "+id+" doesn't exist");
		}
	}
	
	public ProductWithCategoryDTO updateProduct(ProductWithCategoryDTO productDto) {
		Optional<Product> prOptional = this.productRepository.findById(productDto.getId());
		Category category = this.categoryRepository.findByName(productDto.getCategoryName());
		if (prOptional.isPresent() && category!=null) {
			Product _product = prOptional.get();
			_product.setCategory(category);
			_product.setDescription(productDto.getDescription());
			_product.setImage(productDto.getImage());
			_product.setName(productDto.getName());
			_product.setPrice(productDto.getPrice());
			
			return new ProductWithCategoryDTO(this.productRepository.save(_product));
		} else {
			throw new ProductNotFoundException("Product with id: "+productDto.getId()+" doesn't exist");
		}
	}
	
	public void deleteProductById(Long id) {
		this.productRepository.deleteById(id);
	}

}
