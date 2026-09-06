package com.kai.controller.web;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.OtpForm;
import com.kai.service.IUserService;
import com.kai.service.OtpResult;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/verify", "/verify/resend" })
public class VerifyController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");

		String username = pendingUsername(req);
		if (username == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		if (req.getRequestURI().endsWith("/verify/resend")) {
			User user = userService.findByUsername(username);
			if (user != null && user.getStatus() == Constant.STATUS_INACTIVE) {
				try {
					userService.issueOtp(user, "ACTIVATE");
					req.setAttribute("message", "Đã gửi lại mã OTP, vui lòng kiểm tra email.");
				} catch (Exception e) {
					e.printStackTrace();
					req.setAttribute("error", "Không gửi được email. Vui lòng thử lại sau.");
				}
			}
		}

		req.setAttribute("form", new OtpForm());
		req.setAttribute("username", username);
		req.getRequestDispatcher(Constant.DIR_VERIFY).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String username = pendingUsername(req);
		if (username == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		OtpForm form = new OtpForm();
		form.setOtp(req.getParameter("otp") == null ? "" : req.getParameter("otp").trim());

		Map<String, String> errors = ValidationUtil.validate(form);
		if (!errors.isEmpty()) {
			render(req, resp, form, errors, null, username);
			return;
		}

		User user = userService.findByUsername(username);

		if (user != null && user.getStatus() == Constant.STATUS_ACTIVE) {
			req.getSession().removeAttribute("pendingUsername");
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		OtpResult result = userService.checkOtp(user, form.getOtp(), "ACTIVATE");
		String error;

		switch (result) {
			case OK -> {
				user.setStatus(Constant.STATUS_ACTIVE);
				userService.update(user);
				req.getSession().removeAttribute("pendingUsername");
				resp.sendRedirect(req.getContextPath() + "/login?activated=1");
				return;
			}
			case WRONG -> error = "Mã OTP không đúng.";
			case EXPIRED -> error = "Mã OTP đã hết hạn. Bấm \"Gửi lại mã\".";
			case LOCKED -> error = "Bạn đã nhập sai quá nhiều lần. Hãy yêu cầu mã mới.";
			case NOT_FOUND -> error = "Không có yêu cầu xác thực nào đang chờ.";
			default -> error = "Có lỗi xảy ra.";
		}

		render(req, resp, form, errors, error, username);
	}

	private void render(HttpServletRequest req, HttpServletResponse resp, OtpForm form,
			Map<String, String> errors, String globalError, String username)
			throws ServletException, IOException {

		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		req.setAttribute("username", username);
		if (globalError != null) {
			req.setAttribute("error", globalError);
		}
		req.getRequestDispatcher(Constant.DIR_VERIFY).include(req, resp);
	}

	private String pendingUsername(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		Object v = session.getAttribute("pendingUsername");
		return v == null ? null : v.toString();
	}
}
