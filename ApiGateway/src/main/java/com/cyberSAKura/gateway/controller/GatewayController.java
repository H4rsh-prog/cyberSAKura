package com.cyberSAKura.gateway.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@RestController
public class GatewayController {
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired GatewayRoutesProperties routeProperties;
	
	@GetMapping("/")
	public Object status() throws JsonMappingException, JsonProcessingException {
		return this.mapper.readValue(""
				+ "{"
				+ "\"api_gateway\" : \"active\""
				+ "}", Object.class);
	}
	
	@GetMapping("/routes")
	public List<Map<String, String>> getRoutes() {
		return routeProperties.getRoutes();
	}
}

@Component
@ConfigurationProperties(prefix = "spring.cloud.gateway.server.webmvc")
@Data
class GatewayRoutesProperties {
	private List<Map<String,String>> routes = new ArrayList<>();
}
