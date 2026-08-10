package com.tools.module.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
	@PostMapping("compress")
	public byte[] compress(@RequestBody byte[] data) {
		return new byte[0];
		//TO IMPLEMNET
	}
}
