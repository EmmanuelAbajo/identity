package com.esc.identity.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.dto.UserDTO;
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
	
	@Override
	public UserDTO signup(SignupRequestDTO signupDTO) throws EmailExistsException {
		if (userRepository.findByEmail(signupDTO.getEmail()).isPresent()) {
			throw new EmailExistsException(signupDTO.getEmail());
		}
		
		User user = new User();
		
		user.setFirstName(signupDTO.getFirstName());
		user.setLastName(signupDTO.getLastName());
		user.setEmail(signupDTO.getEmail());
		user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
		
		return convertToUserDTO(userRepository.save(user));
	}

	@Override
	public List<UserDTO> findAllUsers() {
		List<UserDTO> users = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(user -> users.add(convertToUserDTO(user)));
		
		return users;
	}
	
	protected UserDTO convertToUserDTO(User user) {
		UserDTO dto = new UserDTO();
		
		dto.setId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setRoles(user.getRoles());
		
		return dto;
	}

}
