package com.pwm.springbootecommerce.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pwm.springbootecommerce.authentication.model.ERole;
import com.pwm.springbootecommerce.authentication.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
