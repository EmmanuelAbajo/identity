package com.esc.identity.exception;

public class EmailExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1949083063399749800L;
	
	public EmailExistsException(String email) {
		super("User with Email " + email + " exists");
	}

}
