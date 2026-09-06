package com.kai.controller.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.OtpResult;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.EmailUtil;
import com.kai.util.PasswordUtil;
import com.kai.util.RateLimiter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/reset-password" })
public class ResetPasswordController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");

		if (resetEmail(req) == null) {
			resp.sendRedirect(req.getContextPath() + "/forgot-password");
			return;
		}
		req.getRequestDispatcher(Constant.DIR_RESET).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String email = resetEmail(req);
		if (email == null) {
			resp.sendRedirect(req.getContextPath() + "/forgot-password");
			return;
		}

		String otp = req.getParameter("otp");
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");
		otp = (otp == null) ? "" : otp.trim();

		String error = validatePassword(password, confirm);
		if (error != null) {
			req.setAttribute("error", error);
			req.getRequestDispatcher(Constant.DIR_RESET).include(req, resp);
			return;
		}

		String key = "reset:mail:" + email;
		if (!RateLimiter.allow(key, 10, 900)) {
			req.setAttribute("error",
					"Bạn đã thử quá nhiều lần. Hãy yêu cầu mã mới sau 15 phút.");
			req.getRequestDispatcher(Constant.DIR_RESET).include(req, resp);
			return;
		}

		User user = userService.findByEmail(email);
		OtpResult result = userService.checkOtp(user, otp, "RESET");

		if (result != OtpResult.OK) {
			switch (result) {
				case EXPIRED -> error = "Mã OTP đã hết hạn. Hãy yêu cầu mã mới.";
				case LOCKED -> error = "Bạn đã nhập sai quá nhiều lần. Hãy yêu cầu mã mới.";
				default -> error = "Mã OTP không đúng.";
			}
			req.setAttribute("error", error);
			req.getRequestDispatcher(Constant.DIR_RESET).include(req, resp);
			return;
		}

		user.setPassword(PasswordUtil.hash(password));
		userService.update(user);

		try {
			EmailUtil.sendPasswordChangedNotice(user.getEmail(), user.getFullname());
		} catch (Exception e) {
			e.printStackTrace();
		}

		RateLimiter.reset(key);
		RateLimiter.reset("login:acc:" + user.getUsername().toLowerCase());

		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		resp.sendRedirect(req.getContextPath() + "/login?reset=1");
	}

	private String resetEmail(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		Object v = session.getAttribute("resetEmail");
		return (v == null) ? null : v.toString();
	}

	private String validatePassword(String password, String confirm) {
		if (password == null || password.length() < 8) {
			return "Mật khẩu tối thiểu 8 ký tự.";
		}
		if (password.getBytes(StandardCharsets.UTF_8).length > PasswordUtil.MAX_PASSWORD_BYTES) {
			return "Mật khẩu quá dài (tối đa 72 byte).";
		}
		if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
			return "Mật khẩu phải có cả chữ và số.";
		}
		if (!password.equals(confirm)) {
			return "Xác nhận mật khẩu không khớp.";
		}
		return null;
	}
}
