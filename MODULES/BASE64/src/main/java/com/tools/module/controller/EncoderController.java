package com.tools.module.controller;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/encode")
public class EncoderController {
	private ObjectMapper mapper = new ObjectMapper();
	private Encoder encoder = Base64.getEncoder();
	private Encoder urlEncoder = Base64.getUrlEncoder();
	
	@PostMapping("/bytes")
	public byte[] encodeBytes(@RequestBody Object data) {
		return this.encoder.encode(this.mapper.writeValueAsBytes(data));
	}
	@PostMapping("/string")
	public String encodeString(@RequestBody Object data) {
		return this.encoder.encodeToString(this.mapper.writeValueAsBytes(data));
	}
	@PostMapping("/url")
	public String encodeUrl(@RequestBody String data) {
		return this.urlEncoder.encodeToString(this.mapper.writeValueAsBytes(data));
	}
	
}
