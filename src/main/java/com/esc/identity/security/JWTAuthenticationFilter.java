package com.esc.identity.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esc.identity.dto.SigninRequestDTO;
import com.esc.identity.dto.SigninResponseDTO;
import com.esc.identity.utility.JWTService;
import com.esc.identity.utility.SecurityConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final AuthenticationManager authManager;
	private final JWTService jwtService;
	private final ObjectMapper objectMapper;
	
	public JWTAuthenticationFilter(AuthenticationManager authManager, JWTService jwtService, ObjectMapper objectMapper) {
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.objectMapper = objectMapper;
		setFilterProcessesUrl(SecurityConstants.LOGIN_URL); 
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		logger.info("Attempting User Authentication");
		try {
			SigninRequestDTO cred = objectMapper.readValue(request.getInputStream(),SigninRequestDTO.class);
			return authManager.authenticate(new UsernamePasswordAuthenticationToken(
					cred.getUsername(),
					cred.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.info("Creating JWT for Logged in User");
		String token = jwtService.generateToken((UserDetails) authResult.getPrincipal());
		
		SigninResponseDTO resp = new SigninResponseDTO();
		resp.setAccessToken(token);
		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		out.print(objectMapper.writeValueAsString(resp));
		out.flush();
	}
}
