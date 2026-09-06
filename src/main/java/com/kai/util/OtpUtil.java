package com.kai.util;

import java.security.SecureRandom;

public class OtpUtil {

	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generate(int length) {
		int size = Math.max(length, 4);
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			sb.append(RANDOM.nextInt(10));
		}
		return sb.toString();
	}
}
