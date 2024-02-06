package com.pwm.springbootecommerce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pwm.springbootecommerce.enums.OrderStatus;
import com.pwm.springbootecommerce.exception.ProductNotFoundException;
import com.pwm.springbootecommerce.model.Address;
import com.pwm.springbootecommerce.model.CartItem;
import com.pwm.springbootecommerce.model.Order;
import com.pwm.springbootecommerce.model.Product;
import com.pwm.springbootecommerce.repository.AddressRepository;
import com.pwm.springbootecommerce.repository.OrderRepository;
import com.pwm.springbootecommerce.repository.ProductRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final AddressRepository addressRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
			AddressRepository addressRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.addressRepository = addressRepository;
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}
	
	public Order createOrder(Order order) {
		validateOrder(order);

		Order orderEntity = new Order();
		List<CartItem> cartItems = new ArrayList<CartItem>();
		for (CartItem cartItem : order.getCartItems()) {
			Optional<Product> opProduct = productRepository.findById(cartItem.getProduct().getId());
			CartItem _cartItem = new CartItem();
			if (opProduct.isEmpty()) {
				throw new ProductNotFoundException(
						"Product with ID:" + cartItem.getProduct().getId() + " doesn't exist");
			}
			_cartItem.setProduct(opProduct.get());
			_cartItem.setQuantity(cartItem.getQuantity());
			_cartItem.setOrder(orderEntity);
			cartItems.add(_cartItem);
		}
		orderEntity.setCartItems(cartItems);
		orderEntity.setGuestEmail(order.getGuestEmail());
		orderEntity.setGuestFullName(order.getGuestFullName());
		orderEntity.setOrderDate(LocalDate.now());
		orderEntity.setTotalAmount(order.getTotalAmount());

		Address _address = addressRepository.findByPostalCode(order.getShippingAddress().getPostalCode());
		
		if (_address == null) {
			_address = new Address();
			_address.setStreet(order.getShippingAddress().getStreet());
			_address.setCity(order.getShippingAddress().getCity());
			_address.setPostalCode(order.getShippingAddress().getPostalCode());
			_address.setCountry(order.getShippingAddress().getCountry());
			addressRepository.save(_address);
			
		}
		orderEntity.setShippingAddress(_address);
		orderEntity.setOrderStatus(OrderStatus.NEW);
		
		
		return orderRepository.save(orderEntity);
	}

	private void validateOrder(Order order) {
		double orderTotal = 0;
		if (stringIsNullOrEmpty(order.getGuestFullName())) {
			throw new IllegalArgumentException("Customer name must not be empty");
		}

		if (stringIsNullOrEmpty(order.getShippingAddress().getPostalCode())
				|| stringIsNullOrEmpty(order.getShippingAddress().getCountry())) {
			throw new IllegalArgumentException("Country name or Postal code must not be empty");
		}

		if (order.getCartItems() == null || order.getCartItems().isEmpty()) {
			throw new IllegalArgumentException("Order must contain at least one item");
		}

		for (CartItem cartItem : order.getCartItems()) {
			if (cartItem.getQuantity() <= 0) {
				throw new IllegalArgumentException("Quantity must be greater than zero");
			}

			Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());
			if (product.isEmpty() || !(product.get().getName().equals(cartItem.getProduct().getName()))
					|| product.get().getPrice() != cartItem.getProduct().getPrice()) {
				
				throw new IllegalArgumentException("Product details is not valid or corrupted");
			}

			orderTotal += product.get().getPrice() * cartItem.getQuantity();

		}
		
		if (Math.abs(getTwoDecimalPlaces(orderTotal) - getTwoDecimalPlaces(order.getTotalAmount())) >= 0.01) {
			throw new IllegalArgumentException("Total price is not correct");
		}

	}

	private boolean stringIsNullOrEmpty(String str) {
		return str == null || str.isBlank();
	}

	private Double getTwoDecimalPlaces(Double d) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
