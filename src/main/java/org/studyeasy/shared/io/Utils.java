package org.studyeasy.shared.io;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private static final Random RANDOM = new SecureRandom();
	private final static String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public  String generateUserId(int length)
	{
		return generateRandomString(length);
	}
	
	public  String generateAddressId(int length)
	{
		return generateRandomString(length);
	}

	private  String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}
}
