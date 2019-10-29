package org.studyeasy.response.model;

public enum ErrorMessages {
	MISSING_REQUIRED_FIELDS("Required fields are missing. Please refer the documentation for required fields."),
	RECORD_ALREADY_EXISTS("Record already exists"),
	EMAIL_ALREADY_EXISTS("Email already exists"),
	INTERNAL_SERVER_ERROR("Internal server error"),
	NO_RECORD_FOUND("Record not found."),
	AUTHENTICATION_FAILED("Authentication failed."),
	COULD_NOT_UPDATE_RECORD("Could not update record."),
	COULD_NOT_DELETE_RECORD("Could not delete record."),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified.");

	private String errorMessage;

	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	

}
