package com.kai.controller.web;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.ForgotPasswordForm;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.RateLimiter;
import com.kai.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/forgot-password" })
public class ForgotPasswordController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.setAttribute("form", new ForgotPasswordForm());
		req.getRequestDispatcher(Constant.DIR_FORGOT).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		ForgotPasswordForm form = new ForgotPasswordForm();
		String email = req.getParameter("email");
		form.setEmail(email == null ? "" : email.trim().toLowerCase());

		Map<String, String> errors = ValidationUtil.validate(form);
		if (!errors.isEmpty()) {
			render(req, resp, form, errors, null);
			return;
		}

		String ipKey = "forgot:ip:" + LoginController.clientIp(req);
		String mailKey = "forgot:mail:" + form.getEmail();

		if (!RateLimiter.allow(ipKey, 10, 900) || !RateLimiter.allow(mailKey, 3, 900)) {
			render(req, resp, form, errors,
					"Bạn đã yêu cầu quá nhiều lần. Vui lòng thử lại sau 15 phút.");
			return;
		}

		User user = userService.findByEmail(form.getEmail());
		if (user != null && user.getStatus() == Constant.STATUS_ACTIVE) {
			try {
				userService.issueOtp(user, "RESET");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		req.getSession(true).setAttribute("resetEmail", form.getEmail());
		resp.sendRedirect(req.getContextPath() + "/reset-password");
	}

	private void render(HttpServletRequest req, HttpServletResponse resp,
			ForgotPasswordForm form, Map<String, String> errors, String globalError)
			throws ServletException, IOException {

		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		if (globalError != null) {
			req.setAttribute("error", globalError);
		}
		req.getRequestDispatcher(Constant.DIR_FORGOT).include(req, resp);
	}
}
