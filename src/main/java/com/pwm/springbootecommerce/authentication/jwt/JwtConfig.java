package com.pwm.springbootecommerce.authentication.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
	private String secret;
	private int expirationMs;
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public int getExpirationMs() {
		return expirationMs;
	}
	public void setExpirationMs(int expirationMs) {
		this.expirationMs = expirationMs;
	}
	@Override
	public String toString() {
		return "JwtConfig [secret=" + secret + ", expirationMs=" + expirationMs + "]";
	}
	
}
