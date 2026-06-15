package com.tools.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class Base64Application {

	public static void main(String[] args) {
		SpringApplication.run(Base64Application.class, args);
	}
}
