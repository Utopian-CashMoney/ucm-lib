package com.ucm.demo.dto;

/**
 * ResponseLoginDto class needed to give response back to
 * a user after they log in which then returns the all user details and 
 * the new generated Jwt Token for new logged in user
 * 
 * @author Charvin Patel
 */


public class ResponseLoginDto {
	private String token;
	private String type = "Bearer";
	private int id;
	private String username;
	private String email;
	private String phNum;
	private String firstName;
	private String lastName;


	public ResponseLoginDto(String accessToken, int id, String username, String email, String phNum, String firstName, String lastName) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.phNum = phNum;
		this.firstName = firstName;
		this.lastName = lastName;
	}



	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPhNum() {
		return phNum;
	}

	public void setPhNum(String phNum) {
		this.phNum = phNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
