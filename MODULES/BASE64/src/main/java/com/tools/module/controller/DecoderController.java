package com.tools.module.controller;

import java.util.Base64;
import java.util.Base64.Decoder;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/decode")
public class DecoderController {
	private ObjectMapper mapper = new ObjectMapper();
	private Decoder decoder = Base64.getDecoder();
	private Decoder urlDecoder = Base64.getUrlDecoder();
	
	@PostMapping("/bytes")
	public byte[] decodeBytes(@RequestBody byte[] data) {
		return this.decoder.decode(data);
	}
	@PostMapping("/string")
	public String encodeString(@RequestBody String data) {
		return this.mapper.writeValueAsString(this.decoder.decode(data));
	}
	@PostMapping("/url")
	public String decodeUrl(@RequestBody String data) {
		return this.mapper.writeValueAsString(this.urlDecoder.decode(data));
	}
	
}
