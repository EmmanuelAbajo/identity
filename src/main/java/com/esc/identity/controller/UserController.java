package com.esc.identity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.entity.User;
import com.esc.identity.service.UserService;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/signup", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public User signUp(@RequestBody SignupRequestDTO signupDTO) {
		return userService.signup(signupDTO);
	}
	
}