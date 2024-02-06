package com.pwm.springbootecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pwm.springbootecommerce.dto.ProductWithCategoryDTO;
import com.pwm.springbootecommerce.model.Category;
import com.pwm.springbootecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategory(Category category);
	List<Product> findByPriceBetween(double minPrice, double maxPrice);
	
	@Query("SELECT new com.pwm.springbootecommerce.dto.ProductWithCategoryDTO(p.id, p.name, p.price, p.description, p.image, c.name) FROM Product p JOIN p.category c")
	List<ProductWithCategoryDTO> findAllProductsWithCategory();
	

}
