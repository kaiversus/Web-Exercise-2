package com.kai.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import com.kai.entity.Category;
import com.kai.entity.Product;
import com.kai.service.ICategoryService;
import com.kai.service.IProductService;
import com.kai.service.impl.CategoryServiceImpl;
import com.kai.service.impl.ProductServiceImpl;
import com.kai.util.CsrfUtil;
import com.kai.util.UploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
		maxFileSize = 5L * 1024 * 1024,
		maxRequestSize = 10L * 1024 * 1024)
@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert",
		"/admin/product/edit", "/admin/product/update", "/admin/product/delete" })
public class ProductController extends HttpServlet {

	private final IProductService productService = new ProductServiceImpl();
	private final ICategoryService categoryService = new CategoryServiceImpl();

	private static final BigDecimal MAX_PRICE = new BigDecimal("999999999999");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String path = req.getRequestURI().substring(req.getContextPath().length());
		req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

		if (path.equals("/admin/product/add")) {
			req.setAttribute("listcate", categoryService.findAll());
			req.getRequestDispatcher("/views/admin/product-add.jsp").include(req, resp);
			return;
		}

		if (path.equals("/admin/product/edit")) {
			int id = parseInt(req.getParameter("id"), 0);
			Product product = productService.findById(id);
			if (product == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/products");
				return;
			}
			req.setAttribute("product", product);
			req.setAttribute("listcate", categoryService.findAll());
			req.getRequestDispatcher("/views/admin/product-edit.jsp").include(req, resp);
			return;
		}

		req.setAttribute("listproduct", productService.findAll());
		req.getRequestDispatcher("/views/admin/product-list.jsp").include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		if (!CsrfUtil.isValid(req)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token khong hop le");
			return;
		}

		String path = req.getRequestURI().substring(req.getContextPath().length());

		switch (path) {
			case "/admin/product/insert" -> insertProduct(req, resp);
			case "/admin/product/update" -> updateProduct(req, resp);
			case "/admin/product/delete" -> deleteProduct(req, resp);
			default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void insertProduct(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Product product = new Product();
		String error = bind(req, product);

		if (error != null) {
			req.setAttribute("error", error);
			req.setAttribute("listcate", categoryService.findAll());
			req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
			req.getRequestDispatcher("/views/admin/product-add.jsp").include(req, resp);
			return;
		}

		product.setCreatedDate(LocalDateTime.now());
		productService.insert(product);
		resp.sendRedirect(req.getContextPath() + "/admin/products");
	}

	private void updateProduct(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int id = parseInt(req.getParameter("productid"), 0);
		Product product = productService.findById(id);
		if (product == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/products");
			return;
		}

		String oldImage = product.getImages();
		String error = bind(req, product);

		if (error != null) {
			req.setAttribute("error", error);
			req.setAttribute("product", product);
			req.setAttribute("listcate", categoryService.findAll());
			req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
			req.getRequestDispatcher("/views/admin/product-edit.jsp").include(req, resp);
			return;
		}

		productService.update(product);

		if (oldImage != null && !oldImage.equals(product.getImages())) {
			UploadUtil.deleteQuietly(oldImage);
		}
		resp.sendRedirect(req.getContextPath() + "/admin/products");
	}

	private void deleteProduct(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		int id = parseInt(req.getParameter("id"), 0);
		Product product = productService.findById(id);
		if (product != null) {
			String image = product.getImages();
			try {
				productService.delete(id);
				UploadUtil.deleteQuietly(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resp.sendRedirect(req.getContextPath() + "/admin/products");
	}

	private String bind(HttpServletRequest req, Product product)
			throws ServletException, IOException {

		String productname = trim(req.getParameter("productname"));
		String description = trim(req.getParameter("description"));
		String priceRaw = trim(req.getParameter("price"));
		int quantity = parseInt(req.getParameter("quantity"), -1);
		int status = "1".equals(req.getParameter("status")) ? 1 : 0;
		int categoryid = parseInt(req.getParameter("categoryid"), 0);

		if (productname.isEmpty() || productname.length() > 255) {
			return "Tên sản phẩm bắt buộc, tối đa 255 ký tự.";
		}
		if (description.length() > 5000) {
			return "Mô tả tối đa 5000 ký tự.";
		}

		BigDecimal price;
		try {
			price = new BigDecimal(priceRaw);
		} catch (Exception e) {
			return "Giá không hợp lệ.";
		}
		if (price.signum() < 0 || price.compareTo(MAX_PRICE) > 0) {
			return "Giá phải từ 0 trở lên và nhỏ hơn 999.999.999.999.";
		}
		if (quantity < 0 || quantity > 1000000) {
			return "Số lượng phải từ 0 đến 1.000.000.";
		}

		Category category = categoryService.findById(categoryid);
		if (category == null) {
			return "Danh mục không tồn tại.";
		}

		Part part = req.getPart("imageFile");
		if (part != null && part.getSize() > 0) {
			try {
				String saved = UploadUtil.saveImage(part);
				if (saved != null) {
					product.setImages(saved);
				}
			} catch (IOException e) {
				return e.getMessage();
			}
		}

		product.setProductname(productname);
		product.setDescription(description);
		product.setPrice(price.setScale(2, RoundingMode.HALF_UP));
		product.setQuantity(quantity);
		product.setStatus(status);
		product.setCategory(category);
		return null;
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}

	private int parseInt(String s, int defaultValue) {
		try {
			return Integer.parseInt(s.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
