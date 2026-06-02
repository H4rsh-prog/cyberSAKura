package com.cyberSAKura.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain getSecurityFilterChain(HttpSecurity http) {
		return http
				.formLogin(Customizer->Customizer.disable())
				.build();
	}
}
