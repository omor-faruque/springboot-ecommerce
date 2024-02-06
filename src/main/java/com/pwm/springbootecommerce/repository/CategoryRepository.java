package com.pwm.springbootecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pwm.springbootecommerce.dto.CategoryDto;
import com.pwm.springbootecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	public boolean existsByName(String name);
	public Category findByName(String name);
	
	@Query("SELECT  new com.pwm.springbootecommerce.dto.CategoryDto(c.id, c.name) FROM Category c")
	public List<CategoryDto> findCategoryNames();
}
