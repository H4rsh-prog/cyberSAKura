package com.cyberSAKura.service.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyberSAKura.service.user.dto.UserEntity;
import com.cyberSAKura.service.user.dto.UserRepository;
import com.cyberSAKura.service.user.exception.UsernameNotFoundException;

public class UserService {
	@Autowired UserRepository repo;
	
	public void saveUser(UserEntity entity) {
		this.repo.save(entity);
	}
	
	public UserEntity getUser(String username) {
		Optional<UserEntity> opt_entity = this.repo.findById(username);
		if(opt_entity.isEmpty()) throw new UsernameNotFoundException();
		return opt_entity.get();
	}
}