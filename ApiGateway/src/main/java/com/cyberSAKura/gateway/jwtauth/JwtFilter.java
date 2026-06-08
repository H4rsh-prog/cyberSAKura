package com.cyberSAKura.gateway.jwtauth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cyberSAKura.gateway.userauth.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired JwtService jwtService;
	@Autowired UserDetailsServiceImpl userDetailsService;
	
	private Logger log = LoggerFactory.getLogger(JwtFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		log.debug("Authorization Header : {"+authHeader+"}");
		String token = null;
		String subject = null;
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			subject = this.jwtService.extractSubject(token);
			log.debug("Subject Extracted : {"+subject+"}");
		}
		if(subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			log.debug("ATTEMPTING AUTHENTICATION");
			UserDetails details = userDetailsService.loadUserByUsername(subject);
			log.debug("User Details Object Loaded : {UserDetails[username:"+details.getUsername()+"; password:"+details.getPassword()+"; authorities:"+details.getAuthorities()+"]}");
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
			log.debug("Created authToken : {"+authToken+"}");
			authToken.setDetails(new WebAuthenticationDetails(request));
			log.debug("Details Set : {"+authToken.getDetails()+"}");
			SecurityContextHolder.getContext().setAuthentication(authToken);
			log.debug("Authenticated Context");
		}
		log.debug("[JWT] Proceeding to next Filter");
		filterChain.doFilter(request, response);
	}
	
}
