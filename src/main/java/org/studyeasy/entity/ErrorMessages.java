package org.studyeasy.entity;

public class ErrorMessages {
	
	public static final String MISSING_REQUIRED_FIELDS = "Required fields are missing. Please refer the documentation for required fields.";
	public static final String RECORD_ALREADY_EXISTS = "Record already exists";
	public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
	public static final String INTERNAL_SERVER_ERROR = "Internal server error";
	public static final String NO_RECORD_FOUND = "Record not found.";
	public static final String AUTHENTICATION_FAILED = "Authentication failed.";
	public static final String COULD_NOT_UPDATE_RECORD = "Could not update record.";
	public static final String COULD_NOT_DELETE_RECORD = "Could not delete record."; 
	public static final String EMAIL_ADDRESS_NOT_VERIFIED = "Email address could not be verified.";

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
