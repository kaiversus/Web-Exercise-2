package com.kai.controller.web;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.ResetPasswordForm;
import com.kai.service.IUserService;
import com.kai.service.OtpResult;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.EmailUtil;
import com.kai.util.PasswordUtil;
import com.kai.util.RateLimiter;
import com.kai.util.ValidationUtil;

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
		req.setAttribute("form", new ResetPasswordForm());
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

		ResetPasswordForm form = new ResetPasswordForm();
		String otp = req.getParameter("otp");
		form.setOtp(otp == null ? "" : otp.trim());
		form.setPassword(req.getParameter("password"));
		form.setConfirmPassword(req.getParameter("confirmPassword"));

		Map<String, String> errors = ValidationUtil.validate(form);
		if (!errors.containsKey("confirmPassword") && !form.passwordMatches()) {
			errors.put("confirmPassword", "Xác nhận mật khẩu không khớp");
		}
		if (!errors.isEmpty()) {
			render(req, resp, form, errors, null);
			return;
		}

		String key = "reset:mail:" + email;
		if (!RateLimiter.allow(key, 10, 900)) {
			render(req, resp, form, errors,
					"Bạn đã thử quá nhiều lần. Hãy yêu cầu mã mới sau 15 phút.");
			return;
		}

		User user = userService.findByEmail(email);
		OtpResult result = userService.checkOtp(user, form.getOtp(), "RESET");

		if (result != OtpResult.OK) {
			String error;
			switch (result) {
				case EXPIRED -> error = "Mã OTP đã hết hạn. Hãy yêu cầu mã mới.";
				case LOCKED -> error = "Bạn đã nhập sai quá nhiều lần. Hãy yêu cầu mã mới.";
				default -> error = "Mã OTP không đúng.";
			}
			render(req, resp, form, errors, error);
			return;
		}

		user.setPassword(PasswordUtil.hash(form.getPassword()));
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

	private void render(HttpServletRequest req, HttpServletResponse resp,
			ResetPasswordForm form, Map<String, String> errors, String globalError)
			throws ServletException, IOException {

		form.setPassword(null);
		form.setConfirmPassword(null);
		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		if (globalError != null) {
			req.setAttribute("error", globalError);
		}
		req.getRequestDispatcher(Constant.DIR_RESET).include(req, resp);
	}

	private String resetEmail(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		Object v = session.getAttribute("resetEmail");
		return (v == null) ? null : v.toString();
	}
}
