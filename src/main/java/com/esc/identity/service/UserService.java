package com.esc.identity.service;

import java.util.List;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.dto.UserDTO;
import com.esc.identity.entity.User;
import com.esc.identity.exception.EmailExistsException;

public interface UserService {
	
	UserDTO signup(SignupRequestDTO signupDTO) throws EmailExistsException;

	List<UserDTO> findAllUsers();

}
