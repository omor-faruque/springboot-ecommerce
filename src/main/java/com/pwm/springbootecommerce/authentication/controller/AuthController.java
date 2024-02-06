package com.pwm.springbootecommerce.authentication.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pwm.springbootecommerce.authentication.jwt.JwtUtils;
import com.pwm.springbootecommerce.authentication.jwt.TokenBlacklist;
import com.pwm.springbootecommerce.authentication.payloads.MessageResponse;
import com.pwm.springbootecommerce.authentication.payloads.SigninRequest;
import com.pwm.springbootecommerce.authentication.payloads.SigninResponse;
import com.pwm.springbootecommerce.authentication.payloads.SignupRequest;
import com.pwm.springbootecommerce.authentication.repository.UserRepository;
import com.pwm.springbootecommerce.authentication.services.AuthService;
import com.pwm.springbootecommerce.authentication.services.UserDetailsImpl;
import com.pwm.springbootecommerce.exception.ErrorItem;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
public class AuthController {

	private final AuthService userService;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final TokenBlacklist tokenBlacklist;

	@Autowired
	public AuthController(AuthService userService, UserRepository userRepository,
			AuthenticationManager authenticationManager, JwtUtils jwtUtils, TokenBlacklist tokenBlacklist) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.tokenBlacklist = tokenBlacklist;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("User with this username already exists."));
		}
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("User with this email already exists."));
		}
		userService.createUser(signupRequest);
		return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = this.jwtUtils.generateJwtToken(authentication);
			
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			return ResponseEntity.ok(
					new SigninResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));

		} catch (Exception e) {
			ErrorItem errorItem = new ErrorItem(new Date(),
					e.getMessage() + ". Error occured for: " + signinRequest.getUsername(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(errorItem, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/signout")
	public ResponseEntity<MessageResponse> signout(@RequestHeader("Authorization") String token) {
		String jwtToken = extractToken(token);
		
		tokenBlacklist.addToBlacklist(jwtToken);
		
		return ResponseEntity.ok(new MessageResponse("Logout successful"));
	}
	
	private String extractToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }

	
	

}
