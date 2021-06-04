package com.ucm.demo.dto;

import javax.validation.constraints.NotBlank;

/**
 * RequestLoginDto class needed to hold the user login details
 * the user enters via login page and sends as a request
 * 
 * @author Charvin Patel
 */


public class RequestLoginDto {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
