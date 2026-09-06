package com.kai.controller.web;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kai.entity.User;
import com.kai.form.ProfileForm;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.UploadUtil;
import com.kai.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/profile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
		maxFileSize = 5L * 1024 * 1024,
		maxRequestSize = 10L * 1024 * 1024)
public class ProfileController extends HttpServlet {

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");

		User sessionUser = currentUser(req);
		User user = userService.findById(sessionUser.getId());

		ProfileForm form = new ProfileForm();
		form.setFullname(user.getFullname());
		form.setPhone(user.getPhone());

		req.setAttribute("form", form);
		req.setAttribute("user", user);
		req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		User sessionUser = currentUser(req);
		User user = userService.findById(sessionUser.getId());

		ProfileForm form = new ProfileForm();
		form.setFullname(trim(req.getParameter("fullname")));
		form.setPhone(trim(req.getParameter("phone")));

		Map<String, String> errors = ValidationUtil.validate(form);

		String savedImage = null;
		try {
			Part part = req.getPart("images");
			savedImage = UploadUtil.saveImage(part);
		} catch (Exception e) {
			errors.put("images", e.getMessage());
		}

		if (!errors.isEmpty()) {
			req.setAttribute("form", form);
			req.setAttribute("errors", errors);
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
			return;
		}

		user.setFullname(form.getFullname());
		user.setPhone(form.getPhone());
		if (savedImage != null) {
			UploadUtil.deleteQuietly(user.getImages());
			user.setImages(savedImage);
		}

		userService.update(user);

		HttpSession session = req.getSession(false);
		sessionUser.setFullname(user.getFullname());
		sessionUser.setPhone(user.getPhone());
		sessionUser.setImages(user.getImages());
		session.setAttribute(Constant.SESSION_ACCOUNT, sessionUser);

		req.setAttribute("form", form);
		req.setAttribute("errors", new LinkedHashMap<String, String>());
		req.setAttribute("message", "Cập nhật hồ sơ thành công!");
		req.setAttribute("user", user);
		req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
	}

	private User currentUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return (User) session.getAttribute(Constant.SESSION_ACCOUNT);
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
