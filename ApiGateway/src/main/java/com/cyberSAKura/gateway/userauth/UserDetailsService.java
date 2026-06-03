package com.cyberSAKura.gateway.userauth;

import java.util.Collection;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired UserRepository repo;
	
	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> entity = repo.findById(username);
		if(entity.isEmpty()) throw new UsernameNotFoundException("no entity associated with username ["+username+"]");
		return new UserDetails(entity.get());
	}

}

class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
	private UserEntity userEntity;
	
	public UserDetails(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable String getPassword() {
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getUsername();
	}
	
}