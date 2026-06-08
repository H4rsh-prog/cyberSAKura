package com.cyberSAKura.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cyberSAKura.gateway.jwtauth.JwtFilter;
import com.cyberSAKura.gateway.userauth.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {
	@Autowired JwtFilter jwtFilter;
	
	@Bean
	SecurityFilterChain getSecurityFilterChain(HttpSecurity http) {
		return http
				.formLogin(FORM -> FORM.disable())
				.csrf(CSRF -> CSRF.disable())
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests(request -> {
					request.requestMatchers(HttpMethod.POST, "/test").permitAll();
					request.requestMatchers(HttpMethod.GET, "/test").authenticated();
					request.anyRequest().authenticated();
				})
				.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}
		
	AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(getUserDetailsService());
		provider.setPasswordEncoder(getBCryptPasswordEncoder());
		return provider;
	}
	
	UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
