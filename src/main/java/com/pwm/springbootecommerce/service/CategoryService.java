package com.pwm.springbootecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pwm.springbootecommerce.dto.CategoryDto;
import com.pwm.springbootecommerce.exception.CategoryNotFoundException;
import com.pwm.springbootecommerce.model.Category;
import com.pwm.springbootecommerce.model.Product;
import com.pwm.springbootecommerce.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> getCategoriesWithProducts() {
		return this.categoryRepository.findAll();
	}
	
	public List<CategoryDto> getCategories() {
		return this.categoryRepository.findCategoryNames();
	}

	public Category addCategory(Category category) {

		if (categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category already exists");
		}

		Category _category = new Category();
		_category.setName(category.getName());

		if (category.getProducts()!=null) {
			List<Product> products = new ArrayList<Product>();

			for (Product product : category.getProducts()) {
				Product _product = new Product();
				_product.setName(product.getName());
				_product.setPrice(product.getPrice());
				_product.setDescription(product.getDescription());
				_product.setImage(product.getImage());
				_product.setCategory(_category);
				products.add(_product);
			}
			_category.setProducts(products);
		}

		return this.categoryRepository.save(_category);

	}

	public Category getCategoryById(Long id) {
		Optional<Category> cOptional = this.categoryRepository.findById(id);
		if (cOptional.isPresent()) {
			return cOptional.get();
		} else {
			throw new CategoryNotFoundException("Category with id: " + id + " dont exist");
		}
	}

	public void deleteCategoryById(Long id) {
		this.categoryRepository.deleteById(id);
	}
}
