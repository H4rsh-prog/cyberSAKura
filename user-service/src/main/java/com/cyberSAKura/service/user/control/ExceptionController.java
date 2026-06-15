package com.cyberSAKura.service.user.control;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cyberSAKura.service.user.exception.UsernameNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ExceptionController {
	private ObjectMapper mapper = new ObjectMapper();
	
	@ExceptionHandler(exception = UsernameNotFoundException.class)
	ResponseEntity<?> usernameNotFoundException() {
		System.err.println("RUNTIME EXCEPTION :: USERNAME NOT FOUND");
		record response(String status, String message) {}
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(new response("failed", "No database entries found associated with the queried username"));
	}
}
