package org.studyeasy.exception;

public class UserServiceException extends RuntimeException {
	private static final long serialVersionUID = -9117622523715045622L;
	public UserServiceException(String message) {
		super(message);
	}
}
