package com.kai.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

	private static final int COST = 12;

	public static final int MAX_PASSWORD_BYTES = 72;

	public static String hash(String plain) {
		if (plain == null) {
			throw new IllegalArgumentException("password null");
		}
		return BCrypt.withDefaults().hashToString(COST, plain.toCharArray());
	}

	public static boolean verify(String plain, String hash) {
		if (plain == null || hash == null || hash.isBlank()) {
			return false;
		}
		try {
			return BCrypt.verifyer()
					.verify(plain.toCharArray(), hash.toCharArray())
					.verified;
		} catch (Exception e) {
			return false;
		}
	}
}
