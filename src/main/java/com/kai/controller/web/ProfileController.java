package com.kai.controller.web;

import java.io.IOException;

import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.impl.UserServiceImpl;
import com.kai.util.Constant;
import com.kai.util.UploadUtil;

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

		String fullname = req.getParameter("fullname");
		String phone = req.getParameter("phone");

		if (fullname != null && fullname.trim().length() <= 150) {
			user.setFullname(fullname.trim());
		}
		if (phone != null && (phone.isBlank() || phone.trim().matches("^0[0-9]{9}$"))) {
			user.setPhone(phone.trim());
		}

		try {
			Part part = req.getPart("images");
			String saved = UploadUtil.saveImage(part);
			if (saved != null) {
				UploadUtil.deleteQuietly(user.getImages());
				user.setImages(saved);
			}
		} catch (Exception e) {
			req.setAttribute("error", "Không lưu được ảnh: " + e.getMessage());
		}

		userService.update(user);

		HttpSession session = req.getSession(false);
		sessionUser.setFullname(user.getFullname());
		sessionUser.setPhone(user.getPhone());
		sessionUser.setImages(user.getImages());
		session.setAttribute(Constant.SESSION_ACCOUNT, sessionUser);

		req.setAttribute("message", "Cập nhật Profile thành công!");
		req.setAttribute("user", user);
		req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
	}

	private User currentUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return (User) session.getAttribute(Constant.SESSION_ACCOUNT);
	}
}
