package com.pwm.springbootecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pwm.springbootecommerce.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Address findByPostalCode(String postalCode);

}
