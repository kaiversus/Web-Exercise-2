package com.kai.controller.web;

import java.io.IOException;

import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.RateLimiter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/forgot-password" })
public class ForgotPasswordController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	private static final String EMAIL_REGEX =
			"^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,190}\\.[A-Za-z]{2,}$";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.getRequestDispatcher(Constant.DIR_FORGOT).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String email = req.getParameter("email");
		email = (email == null) ? "" : email.trim().toLowerCase();

		if (!email.matches(EMAIL_REGEX)) {
			req.setAttribute("error", "Email không hợp lệ.");
			req.getRequestDispatcher(Constant.DIR_FORGOT).include(req, resp);
			return;
		}

		String ipKey = "forgot:ip:" + LoginController.clientIp(req);
		String mailKey = "forgot:mail:" + email;

		if (!RateLimiter.allow(ipKey, 10, 900) || !RateLimiter.allow(mailKey, 3, 900)) {
			req.setAttribute("error",
					"Bạn đã yêu cầu quá nhiều lần. Vui lòng thử lại sau 15 phút.");
			req.getRequestDispatcher(Constant.DIR_FORGOT).include(req, resp);
			return;
		}

		User user = userService.findByEmail(email);
		if (user != null && user.getStatus() == Constant.STATUS_ACTIVE) {
			try {
				userService.issueOtp(user, "RESET");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		req.getSession(true).setAttribute("resetEmail", email);
		resp.sendRedirect(req.getContextPath() + "/reset-password");
	}
}
