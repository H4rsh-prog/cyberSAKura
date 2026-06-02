package com.cyberSAKura.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GatewayController {
	private ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("/")
	public Object status() throws JsonMappingException, JsonProcessingException {
		return this.mapper.readValue(""
				+ "{"
				+ "\"api_gateway\" : \"active\""
				+ "}", Object.class);
	}
}
