package com.tools.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Base64Application {

	public static void main(String[] args) {
		SpringApplication.run(Base64Application.class, args);
	}
	
	@Value("${server.servlet.context-path}") String name;
	@PostConstruct
	public void g() {
		System.err.println(name);
	}
}
