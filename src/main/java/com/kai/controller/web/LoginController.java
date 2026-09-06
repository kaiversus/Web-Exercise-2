package com.kai.controller.web;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.LoginForm;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.CsrfUtil;
import com.kai.util.PasswordUtil;
import com.kai.util.RateLimiter;
import com.kai.util.ValidationUtil;

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

		LoginForm form = new LoginForm();

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (Constant.COOKIE_REMEMBER.equals(c.getName())) {
					form.setAccount(c.getValue());
				}
			}
		}

		if ("1".equals(req.getParameter("activated"))) {
			req.setAttribute("message", "Kích hoạt thành công. Mời bạn đăng nhập.");
		}
		if ("1".equals(req.getParameter("reset"))) {
			req.setAttribute("message", "Đổi mật khẩu thành công. Mời bạn đăng nhập.");
		}

		req.setAttribute("form", form);
		req.setAttribute("redirect", safePath(req.getParameter("redirect")));
		req.getRequestDispatcher(Constant.DIR_LOGIN).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		LoginForm form = new LoginForm();
		form.setAccount(trim(req.getParameter("account")));
		form.setPassword(req.getParameter("password"));

		boolean remember = "on".equals(req.getParameter("remember"));
		String redirect = safePath(req.getParameter("redirect"));

		Map<String, String> errors = ValidationUtil.validate(form);
		if (!errors.isEmpty()) {
			render(req, resp, form, errors, null, redirect);
			return;
		}

		String account = form.getAccount();
		String ipKey = "login:ip:" + clientIp(req);
		String accKey = "login:acc:" + account.toLowerCase();

		if (!RateLimiter.allow(ipKey, 20, 300) || !RateLimiter.allow(accKey, 5, 300)) {
			long wait = Math.max(RateLimiter.retryAfterSeconds(ipKey, 300),
					RateLimiter.retryAfterSeconds(accKey, 300));
			render(req, resp, form, errors,
					"Bạn đã thử quá nhiều lần. Vui lòng đợi " + wait + " giây.", redirect);
			return;
		}

		User user = userService.findByUsername(account);
		if (user == null) {
			user = userService.findByEmail(account.toLowerCase());
		}

		String storedHash = (user == null) ? DUMMY_HASH : user.getPassword();
		boolean passwordOk = PasswordUtil.verify(form.getPassword(), storedHash);

		if (user == null || !passwordOk) {
			render(req, resp, form, errors,
					"Tên đăng nhập hoặc mật khẩu không đúng.", redirect);
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

	private void render(HttpServletRequest req, HttpServletResponse resp, LoginForm form,
			Map<String, String> errors, String globalError, String redirect)
			throws ServletException, IOException {

		form.setPassword(null);
		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		req.setAttribute("redirect", redirect);
		if (globalError != null) {
			req.setAttribute("error", globalError);
		}
		req.getRequestDispatcher(Constant.DIR_LOGIN).include(req, resp);
	}

	public static String clientIp(HttpServletRequest req) {
		return req.getRemoteAddr();
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
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
