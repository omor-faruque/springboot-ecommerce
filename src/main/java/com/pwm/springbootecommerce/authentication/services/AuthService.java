package com.pwm.springbootecommerce.authentication.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pwm.springbootecommerce.authentication.model.ERole;
import com.pwm.springbootecommerce.authentication.model.Role;
import com.pwm.springbootecommerce.authentication.model.User;
import com.pwm.springbootecommerce.authentication.payloads.SignupRequest;
import com.pwm.springbootecommerce.authentication.repository.RoleRepository;
import com.pwm.springbootecommerce.authentication.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder encoder;

	@Autowired
	public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
	}

	public void createUser(SignupRequest signupRequest) {
		Set<Role> roles = getUserRoles(ERole.ROLE_USER);
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()), roles);
		this.userRepository.save(user);
	}

	public User createAdminUser(User user) {

		Set<Role> roles = getUserRoles(ERole.ROLE_ADMIN);

		user.setRoles(roles);

		// TO DO: SET who created

		return this.userRepository.save(user);
	}

	private Set<Role> getUserRoles(ERole role) {
		Set<Role> roles = new HashSet<Role>();

		Role adminRole = this.roleRepository.findByName(ERole.ROLE_ADMIN).get();
		Role userRole = this.roleRepository.findByName(ERole.ROLE_USER).get();

		if (role == ERole.ROLE_ADMIN && adminRole != null) {
			roles.add(adminRole);
		}

		if (userRole != null) {
			roles.add(userRole);
		}
		return roles;
	}
}
