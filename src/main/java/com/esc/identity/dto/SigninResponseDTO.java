package com.esc.identity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SigninResponseDTO {
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("token_type")
	private String tokenType = "bearer";
	
	@JsonProperty("expires_in")
	private long expiration = 3600;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
