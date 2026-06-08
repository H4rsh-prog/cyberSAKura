package com.cyberSAKura.gateway.jwtauth;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.cyberSAKura.gateway.userauth.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tools.jackson.databind.ObjectMapper;

@Service
public class JwtService {
	private String secretKey = "thisisalongenoughsecretkeytobeatleast256bytes";
	
	public JwtService() {
		this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
	}
	
	public Key getKey() {
		return Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.secretKey));
	}
	
	public String generateToken(UserEntity entity) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("entity", entity);
		return Jwts.builder()
				.setSubject(entity.getUsername())
				.addClaims(claims)
				.setIssuedAt(new Date())
				.signWith(getKey())
				.compact();
	}
	
	public Claims extractClaims(String token) {
		return (Claims) Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parse(token)
				.getBody();
	}
	
	public <R> R resolveClaim(String token, Function<Claims, R> resolver) {
		return resolver.apply(extractClaims(token));
	}
	
	public String extractSubject(String token) {
		return resolveClaim(token, Claims::getSubject);
	}
}
