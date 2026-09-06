package com.kai.controller.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/logout" })
public class LogoutController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doLogout(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doLogout(req, resp);
	}

	private void doLogout(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		Cookie kill = new Cookie("JSESSIONID", "");
		kill.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
		kill.setHttpOnly(true);
		kill.setMaxAge(0);
		resp.addCookie(kill);

		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setHeader("Pragma", "no-cache");
		resp.sendRedirect(req.getContextPath() + "/home");
	}
}
