package org.studyeasy.response.model;

import java.util.List;

public class UserResponseModel {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddresseResponseModel> addresses;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AddresseResponseModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddresseResponseModel> addresses) {
		this.addresses = addresses;
	}

}
