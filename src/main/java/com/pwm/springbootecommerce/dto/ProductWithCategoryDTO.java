package com.pwm.springbootecommerce.dto;

import com.pwm.springbootecommerce.model.Product;

public class ProductWithCategoryDTO {
	private Long id;
    private String name;
    private double price;
    private String description;
    private String image;
    private String categoryName;
    
    public ProductWithCategoryDTO() {}
    
	public ProductWithCategoryDTO(Long id, String name, double price, String description, String image,
			String categoryName) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.image = image;
		this.categoryName = categoryName;
	}
	
	public ProductWithCategoryDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.description = product.getDescription();
		this.image = product.getImage();
		this.categoryName = product.getCategory().getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
    
    
	
    
}

