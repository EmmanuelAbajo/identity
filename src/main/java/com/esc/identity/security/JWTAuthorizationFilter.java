package com.esc.identity.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.esc.identity.utility.JWTService;
import com.esc.identity.utility.SecurityConstants;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private final JWTService jwtService;
	private final UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authManager, JWTService jwtService,  UserDetailsService userDetailsService) {
		super(authManager);
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String token = null;
		String username = null;

		String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
		if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			token = authHeader.substring(7);
			username = jwtService.extractSubject(token);
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		chain.doFilter(request, response);
	}
}
