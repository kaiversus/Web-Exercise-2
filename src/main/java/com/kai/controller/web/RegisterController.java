package com.kai.controller.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{4,30}$";
	private static final String EMAIL_REGEX =
			"^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,190}\\.[A-Za-z]{2,}$";
	private static final String PHONE_REGEX = "^0[0-9]{9}$";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.getRequestDispatcher(Constant.DIR_REGISTER).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String username = trim(req.getParameter("username"));
		String email = trim(req.getParameter("email")).toLowerCase();
		String fullname = trim(req.getParameter("fullname"));
		String phone = trim(req.getParameter("phone"));
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");

		String error = validate(username, email, phone, password, confirm);

		User byUsername = (error == null) ? userService.findByUsername(username) : null;
		User byEmail = (error == null) ? userService.findByEmail(email) : null;

		if (error == null && byUsername != null
				&& byUsername.getStatus() == Constant.STATUS_ACTIVE) {
			error = "Tên đăng nhập đã được sử dụng.";
		}
		if (error == null && byEmail != null
				&& byEmail.getStatus() == Constant.STATUS_ACTIVE) {
			error = "Email đã được đăng ký.";
		}
		if (error == null && byUsername != null && byEmail != null
				&& byUsername.getId() != byEmail.getId()) {
			error = "Tên đăng nhập và email đang thuộc hai tài khoản chờ xác thực khác nhau.";
		}

		if (error != null) {
			req.setAttribute("error", error);
			req.setAttribute("username", username);
			req.setAttribute("email", email);
			req.setAttribute("fullname", fullname);
			req.setAttribute("phone", phone);
			req.getRequestDispatcher(Constant.DIR_REGISTER).include(req, resp);
			return;
		}

		User user = (byUsername != null) ? byUsername : (byEmail != null ? byEmail : new User());
		user.setUsername(username);
		user.setEmail(email);
		user.setFullname(fullname);
		user.setPhone(phone);

		try {
			userService.register(user, password);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error",
					"Không gửi được email xác thực. Vui lòng thử lại sau.");
			req.setAttribute("username", username);
			req.setAttribute("email", email);
			req.setAttribute("fullname", fullname);
			req.setAttribute("phone", phone);
			req.getRequestDispatcher(Constant.DIR_REGISTER).include(req, resp);
			return;
		}

		req.getSession(true).setAttribute("pendingUsername", username);
		resp.sendRedirect(req.getContextPath() + "/verify");
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}

	private String validate(String username, String email, String phone,
			String password, String confirm) {

		if (!username.matches(USERNAME_REGEX)) {
			return "Tên đăng nhập 4-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới.";
		}
		if (!email.matches(EMAIL_REGEX)) {
			return "Email không hợp lệ.";
		}
		if (!phone.isEmpty() && !phone.matches(PHONE_REGEX)) {
			return "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng 0.";
		}
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
