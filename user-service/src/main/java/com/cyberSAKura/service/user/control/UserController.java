package com.cyberSAKura.service.user.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyberSAKura.service.user.dto.UserEntity;
import com.cyberSAKura.service.user.service.UserService;

@RestController
public class UserController {
	@Autowired UserService service;
	
	@GetMapping("fetch")
	public UserEntity fetchUser(@RequestParam("username") String username) {
		return this.service.getUser(username);
	}
}
