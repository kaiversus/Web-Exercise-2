package com.kai.controller.web;

import java.io.IOException;
import java.util.List;

import com.kai.entity.Product;
import com.kai.service.IProductService;
import com.kai.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/product", "/product/detail" })
public class ProductWebController extends HttpServlet {

	private static final int PAGE_SIZE = 6;

	private final IProductService productService = new ProductServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String path = req.getRequestURI().substring(req.getContextPath().length());

		if (path.equals("/product/detail")) {
			showDetail(req, resp);
		} else {
			showList(req, resp);
		}
	}

	private void showList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int totalPages = Math.max(productService.totalActivePages(PAGE_SIZE), 1);

		int page = parseInt(req.getParameter("page"), 1);
		if (page < 1) {
			page = 1;
		}
		if (page > totalPages) {
			page = totalPages;
		}

		List<Product> list = productService.findActiveByPage(page, PAGE_SIZE);

		req.setAttribute("products", list);
		req.setAttribute("currentPage", page);
		req.setAttribute("totalPages", totalPages);
		req.setAttribute("totalItems", productService.countActive());
		req.getRequestDispatcher("/views/product.jsp").include(req, resp);
	}

	private void showDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int id = parseInt(req.getParameter("id"), 0);
		Product product = productService.findActiveById(id);

		if (product == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			req.setAttribute("error", "Sản phẩm không tồn tại hoặc đã ngừng kinh doanh.");
			req.getRequestDispatcher("/views/product-detail.jsp").include(req, resp);
			return;
		}

		req.setAttribute("product", product);
		req.getRequestDispatcher("/views/product-detail.jsp").include(req, resp);
	}

	private int parseInt(String s, int defaultValue) {
		try {
			return Integer.parseInt(s.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
