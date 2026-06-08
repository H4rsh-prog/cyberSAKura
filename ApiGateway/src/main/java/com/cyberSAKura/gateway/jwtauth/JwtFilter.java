package com.cyberSAKura.gateway.jwtauth;

import java.io.IOException;

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
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String subject = null;
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			subject = this.jwtService.extractSubject(token);
			System.err.println("NAME EXTRACTED FROM AUTH TOKEN  : "+subject);
		}
		if(subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails details = userDetailsService.loadUserByUsername(subject);
			System.err.println("USER DETAILS EXTRACTED FROM AUTH TOKEN  : ENTITY[ username:"+details.getUsername()+"; password:"+details.getPassword()+"]");
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
			System.err.println("AUTHENTICATED : "+SecurityContextHolder.getContext().getAuthentication()!=null);
		}
		filterChain.doFilter(request, response);
	}
	
}
