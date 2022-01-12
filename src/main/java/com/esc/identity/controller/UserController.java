package com.esc.identity.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.dto.UserDTO;
import com.esc.identity.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/signup", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO signUp(@RequestBody SignupRequestDTO signupDTO) {
		return userService.signup(signupDTO);
	}
	
	@GetMapping(consumes = "application/json", produces = "application/json")
	public Collection<UserDTO> getAllUsers() {
		return userService.findAllUsers();
	}
	
}