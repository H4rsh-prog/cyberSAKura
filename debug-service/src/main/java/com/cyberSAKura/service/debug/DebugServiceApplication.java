package com.cyberSAKura.service.debug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DebugServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebugServiceApplication.class, args);
	}

}
