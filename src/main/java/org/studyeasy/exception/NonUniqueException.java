package org.studyeasy.exception;

public class NonUniqueException extends RuntimeException {
	private static final long serialVersionUID = -6541725424411504272L;
	public NonUniqueException(String resultCount) {
		super(resultCount);
	}
}
