package com.esc.identity.service;

import com.esc.identity.dto.SignupRequestDTO;
import com.esc.identity.entity.User;
import com.esc.identity.exception.EmailExistsException;

public interface UserService {
	
	User signup(SignupRequestDTO signupDTO) throws EmailExistsException;

}
