package com.cyberSAKura.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class GatewayController {
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired DiscoveryClient lbClient;
	
	@GetMapping("/")
	public Object routes() throws JsonMappingException, JsonProcessingException {
		String json = "[";
		List<String> serviceList = lbClient.getServices();
		for(int i=0;i<serviceList.size();i++) {
			json += "{\"serviceName\" : \"" + serviceList.get(i) +"\" ,"
					+ "\"serviceInstances \" : "+ mapper.writeValueAsString(lbClient.getInstances(serviceList.get(i))) 
					+ ((i==(serviceList.size()-1))?"}":"},");
		}
		json += "]";
		return mapper.readValue(json, Object.class);
	}
}

