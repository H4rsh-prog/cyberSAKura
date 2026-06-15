package com.cyberSAKura.service.user.service;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberSAKura.service.user.dto.UserEntity;
import com.cyberSAKura.service.user.dto.UserRepository;
import com.cyberSAKura.service.user.exception.UsernameNotFoundException;

@Service
public class UserService {
	@Autowired UserRepository repo;
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public void saveUser(UserEntity entity) {
		this.repo.save(entity);
	}
	
	public UserEntity getUser(String username) {
		Optional<UserEntity> opt_entity = this.repo.findById(username);
		log.info("FETCH REQUEST QUERY ["+username+"] HANDLED WITH THE FOLLOWING ENTITY : "+opt_entity);
		if(opt_entity.isEmpty()) throw new UsernameNotFoundException();
		return opt_entity.get();
	}
}