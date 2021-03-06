package com.esc.identity.utility;

public class SecurityConstants {
	
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/v1/users/signup";
    public static final String LOGIN_URL = "/api/v1/users/signin";
    public static final long EXPIRATION_TIME = 60;

}
