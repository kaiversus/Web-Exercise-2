package com.kai.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.kai.entity.User;
import com.kai.util.Constant;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/admin/*", "/profile" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String path = req.getRequestURI().substring(req.getContextPath().length());

		HttpSession session = req.getSession(false);
		User user = (session == null)
				? null
				: (User) session.getAttribute(Constant.SESSION_ACCOUNT);

		if (user == null) {
			String query = req.getQueryString();
			String target = (query == null) ? path : path + "?" + query;
			resp.sendRedirect(req.getContextPath() + "/login?redirect="
					+ URLEncoder.encode(target, StandardCharsets.UTF_8));
			return;
		}

		if (path.startsWith("/admin") && user.getRole() != Constant.ROLE_ADMIN) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Ban khong co quyen truy cap");
			return;
		}

		chain.doFilter(request, response);
	}
}
