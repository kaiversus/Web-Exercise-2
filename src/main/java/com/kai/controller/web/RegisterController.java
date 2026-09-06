package com.kai.controller.web;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.RegisterForm;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.setAttribute("form", new RegisterForm());
		req.getRequestDispatcher(Constant.DIR_REGISTER).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		RegisterForm form = new RegisterForm();
		form.setUsername(trim(req.getParameter("username")));
		form.setEmail(trim(req.getParameter("email")).toLowerCase());
		form.setFullname(trim(req.getParameter("fullname")));
		form.setPhone(trim(req.getParameter("phone")));
		form.setPassword(req.getParameter("password"));
		form.setConfirmPassword(req.getParameter("confirmPassword"));

		Map<String, String> errors = ValidationUtil.validate(form);

		if (!errors.containsKey("confirmPassword") && !form.passwordMatches()) {
			errors.put("confirmPassword", "Xác nhận mật khẩu không khớp");
		}

		User byUsername = userService.findByUsername(form.getUsername());
		User byEmail = userService.findByEmail(form.getEmail());

		if (byUsername != null && byUsername.getStatus() == Constant.STATUS_ACTIVE) {
			errors.put("username", "Tên đăng nhập đã được sử dụng");
		}
		if (byEmail != null && byEmail.getStatus() == Constant.STATUS_ACTIVE) {
			errors.put("email", "Email đã được đăng ký");
		}
		if (byUsername != null && byEmail != null && byUsername.getId() != byEmail.getId()) {
			errors.put("username",
					"Tên đăng nhập và email đang thuộc hai tài khoản chờ xác thực khác nhau");
		}

		if (!errors.isEmpty()) {
			renderForm(req, resp, form, errors, null);
			return;
		}

		User user = (byUsername != null) ? byUsername : (byEmail != null ? byEmail : new User());
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());
		user.setFullname(form.getFullname());
		user.setPhone(form.getPhone());

		try {
			userService.register(user, form.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			renderForm(req, resp, form, errors,
					"Không gửi được email xác thực. Vui lòng thử lại sau.");
			return;
		}

		req.getSession(true).setAttribute("pendingUsername", form.getUsername());
		resp.sendRedirect(req.getContextPath() + "/verify");
	}

	private void renderForm(HttpServletRequest req, HttpServletResponse resp,
			RegisterForm form, Map<String, String> errors, String globalError)
			throws ServletException, IOException {

		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		if (globalError != null) {
			req.setAttribute("error", globalError);
		}
		req.getRequestDispatcher(Constant.DIR_REGISTER).include(req, resp);
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
