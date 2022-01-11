package com.esc.identity.service.impl;

import java.util.Arrays;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.entity.User;
import com.esc.identity.exception.EmailExistsException;
import com.esc.identity.repository.RoleRepository;
import com.esc.identity.repository.UserRepository;
import com.esc.identity.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User signup(SignupRequestDTO signupDTO) throws EmailExistsException {
		if (userRepository.findByEmail(signupDTO.getEmail()).isPresent()) {
			throw new EmailExistsException(signupDTO.getEmail());
		}
		
		User user = new User();
		
		user.setFirstName(signupDTO.getFirstName());
		user.setLastName(signupDTO.getLastName());
		user.setEmail(signupDTO.getEmail());
		user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
		
		return userRepository.save(user);
	}

}
