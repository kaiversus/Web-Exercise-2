package com.kai.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

import com.kai.entity.Category;
import com.kai.entity.Product;
import com.kai.form.ProductForm;
import com.kai.service.ICategoryService;
import com.kai.service.IProductService;
import com.kai.service.impl.CategoryServiceImpl;
import com.kai.service.impl.ProductServiceImpl;
import com.kai.util.CsrfUtil;
import com.kai.util.UploadUtil;
import com.kai.util.ValidationUtil;

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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String path = req.getRequestURI().substring(req.getContextPath().length());
		req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

		if (path.equals("/admin/product/add")) {
			req.setAttribute("form", new ProductForm());
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
			req.setAttribute("form", toForm(product));
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

		ProductForm form = bindForm(req);
		Map<String, String> errors = ValidationUtil.validate(form);

		Category category = categoryService.findById(form.getCategoryid());
		if (category == null) {
			errors.put("categoryid", "Danh mục không tồn tại");
		}

		String savedImage = null;
		try {
			savedImage = saveImageIfAny(req);
		} catch (Exception e) {
			errors.put("imageFile", e.getMessage());
		}

		if (!errors.isEmpty()) {
			render(req, resp, form, errors, "/views/admin/product-add.jsp", null);
			return;
		}

		Product product = new Product();
		apply(product, form, category, savedImage);
		product.setCreatedDate(LocalDateTime.now());
		productService.insert(product);
		resp.sendRedirect(req.getContextPath() + "/admin/products");
	}

	private void updateProduct(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ProductForm form = bindForm(req);
		Product product = productService.findById(form.getProductid());
		if (product == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/products");
			return;
		}

		Map<String, String> errors = ValidationUtil.validate(form);

		Category category = categoryService.findById(form.getCategoryid());
		if (category == null) {
			errors.put("categoryid", "Danh mục không tồn tại");
		}

		String savedImage = null;
		try {
			savedImage = saveImageIfAny(req);
		} catch (Exception e) {
			errors.put("imageFile", e.getMessage());
		}

		if (!errors.isEmpty()) {
			render(req, resp, form, errors, "/views/admin/product-edit.jsp", product);
			return;
		}

		String oldImage = product.getImages();
		apply(product, form, category, savedImage);
		productService.update(product);

		if (savedImage != null && oldImage != null && !oldImage.equals(savedImage)) {
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

	private ProductForm bindForm(HttpServletRequest req) {
		ProductForm form = new ProductForm();
		form.setProductid(parseInt(req.getParameter("productid"), 0));
		form.setProductname(trim(req.getParameter("productname")));
		form.setDescription(trim(req.getParameter("description")));
		form.setQuantity(parseInt(req.getParameter("quantity"), -1));
		form.setCategoryid(parseInt(req.getParameter("categoryid"), 0));
		form.setStatus("1".equals(req.getParameter("status")) ? 1 : 0);
		form.setPrice(parseDecimal(req.getParameter("price")));
		return form;
	}

	private ProductForm toForm(Product product) {
		ProductForm form = new ProductForm();
		form.setProductid(product.getProductid());
		form.setProductname(product.getProductname());
		form.setDescription(product.getDescription());
		form.setPrice(product.getPrice());
		form.setQuantity(product.getQuantity());
		form.setStatus(product.getStatus());
		if (product.getCategory() != null) {
			form.setCategoryid(product.getCategory().getCategoryid());
		}
		return form;
	}

	private void apply(Product product, ProductForm form, Category category, String savedImage) {
		product.setProductname(form.getProductname());
		product.setDescription(form.getDescription());
		product.setPrice(form.getPrice().setScale(2, RoundingMode.HALF_UP));
		product.setQuantity(form.getQuantity());
		product.setStatus(form.getStatus());
		product.setCategory(category);
		if (savedImage != null) {
			product.setImages(savedImage);
		}
	}

	private String saveImageIfAny(HttpServletRequest req) throws Exception {
		Part part = req.getPart("imageFile");
		if (part == null || part.getSize() == 0) {
			return null;
		}
		return UploadUtil.saveImage(part);
	}

	private void render(HttpServletRequest req, HttpServletResponse resp, ProductForm form,
			Map<String, String> errors, String view, Product product)
			throws ServletException, IOException {

		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		req.setAttribute("listcate", categoryService.findAll());
		req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
		if (product != null) {
			req.setAttribute("product", product);
		}
		req.getRequestDispatcher(view).include(req, resp);
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

	private BigDecimal parseDecimal(String s) {
		try {
			return new BigDecimal(s.trim());
		} catch (Exception e) {
			return null;
		}
	}
}
