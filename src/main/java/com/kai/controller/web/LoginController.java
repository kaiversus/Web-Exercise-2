package com.kai.controller.web;

import java.io.IOException;

import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.CsrfUtil;
import com.kai.util.PasswordUtil;
import com.kai.util.RateLimiter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	private static final String DUMMY_HASH =
			"$2a$12$usesomesillystringfore2uDLvp1Ii2e./U9C8sBjqp8I90dH6hi";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");

		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
			resp.sendRedirect(req.getContextPath() + "/home");
			return;
		}

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (Constant.COOKIE_REMEMBER.equals(c.getName())) {
					req.setAttribute("rememberedUsername", c.getValue());
				}
			}
		}

		if ("1".equals(req.getParameter("activated"))) {
			req.setAttribute("message", "Kích hoạt thành công. Mời bạn đăng nhập.");
		}
		if ("1".equals(req.getParameter("reset"))) {
			req.setAttribute("message", "Đổi mật khẩu thành công. Mời bạn đăng nhập.");
		}

		req.setAttribute("redirect", safePath(req.getParameter("redirect")));
		req.getRequestDispatcher(Constant.DIR_LOGIN).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String account = req.getParameter("account");
		String password = req.getParameter("password");
		boolean remember = "on".equals(req.getParameter("remember"));
		String redirect = safePath(req.getParameter("redirect"));

		if (account == null) {
			account = "";
		}
		if (password == null) {
			password = "";
		}
		account = account.trim();

		String ipKey = "login:ip:" + clientIp(req);
		String accKey = "login:acc:" + account.toLowerCase();

		if (!RateLimiter.allow(ipKey, 20, 300) || !RateLimiter.allow(accKey, 5, 300)) {
			long wait = Math.max(RateLimiter.retryAfterSeconds(ipKey, 300),
					RateLimiter.retryAfterSeconds(accKey, 300));
			req.setAttribute("error",
					"Bạn đã thử quá nhiều lần. Vui lòng đợi " + wait + " giây.");
			req.setAttribute("account", account);
			req.setAttribute("redirect", redirect);
			req.getRequestDispatcher(Constant.DIR_LOGIN).include(req, resp);
			return;
		}

		User user = userService.findByUsername(account);
		if (user == null) {
			user = userService.findByEmail(account.toLowerCase());
		}

		String storedHash = (user == null) ? DUMMY_HASH : user.getPassword();
		boolean passwordOk = PasswordUtil.verify(password, storedHash);

		if (user == null || !passwordOk) {
			req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
			req.setAttribute("account", account);
			req.setAttribute("redirect", redirect);
			req.getRequestDispatcher(Constant.DIR_LOGIN).include(req, resp);
			return;
		}

		if (user.getStatus() != Constant.STATUS_ACTIVE) {
			req.getSession(true).setAttribute("pendingUsername", user.getUsername());
			resp.sendRedirect(req.getContextPath() + "/verify");
			return;
		}

		HttpSession oldSession = req.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();
		}
		HttpSession session = req.getSession(true);

		User safeUser = new User();
		safeUser.setId(user.getId());
		safeUser.setUsername(user.getUsername());
		safeUser.setEmail(user.getEmail());
		safeUser.setFullname(user.getFullname());
		safeUser.setPhone(user.getPhone());
		safeUser.setImages(user.getImages());
		safeUser.setRole(user.getRole());
		safeUser.setStatus(user.getStatus());

		session.setAttribute(Constant.SESSION_ACCOUNT, safeUser);
		session.setMaxInactiveInterval(30 * 60);
		CsrfUtil.getToken(session);

		Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER,
				remember ? user.getUsername() : "");
		cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
		cookie.setHttpOnly(true);
		cookie.setMaxAge(remember ? 30 * 24 * 60 * 60 : 0);
		resp.addCookie(cookie);

		RateLimiter.reset(accKey);

		resp.sendRedirect(redirect.isEmpty()
				? req.getContextPath() + "/home"
				: req.getContextPath() + redirect);
	}

	public static String clientIp(HttpServletRequest req) {
		return req.getRemoteAddr();
	}

	private String safePath(String target) {
		if (target == null || target.isBlank()) {
			return "";
		}
		if (!target.startsWith("/")) {
			return "";
		}
		if (target.startsWith("//")) {
			return "";
		}
		if (target.contains(":") || target.contains("\\")) {
			return "";
		}
		if (target.contains("\r") || target.contains("\n")) {
			return "";
		}
		return target;
	}
}
