package com.kai.controller.web;

import java.io.IOException;

import com.kai.service.IProductService;
import com.kai.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/home")
public class HomeController extends HttpServlet {

	private final IProductService productService = new ProductServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");
		req.setAttribute("newestProducts", productService.findActiveNewest(10));
		req.getRequestDispatcher("/views/home.jsp").include(req, resp);
	}
}
