package com.kai.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CsrfUtil {

	public static final String TOKEN_KEY = "csrfToken";
	public static final String FIELD_NAME = "_csrf";

	private static final SecureRandom RANDOM = new SecureRandom();

	public static String getToken(HttpSession session) {
		Object token = session.getAttribute(TOKEN_KEY);
		if (token == null) {
			byte[] bytes = new byte[32];
			RANDOM.nextBytes(bytes);
			token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
			session.setAttribute(TOKEN_KEY, token);
		}
		return token.toString();
	}

	public static void rotate(HttpSession session) {
		session.removeAttribute(TOKEN_KEY);
		getToken(session);
	}

	public static boolean isValid(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return false;
		}
		Object expected = session.getAttribute(TOKEN_KEY);
		String actual = req.getParameter(FIELD_NAME);
		if (expected == null || actual == null) {
			return false;
		}
		return MessageDigest.isEqual(
				expected.toString().getBytes(StandardCharsets.UTF_8),
				actual.getBytes(StandardCharsets.UTF_8));
	}
}
