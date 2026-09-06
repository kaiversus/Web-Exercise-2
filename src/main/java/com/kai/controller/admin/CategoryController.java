package com.kai.controller.admin;

import java.io.IOException;
import java.util.Map;

import com.kai.entity.Category;
import com.kai.form.CategoryForm;
import com.kai.service.ICategoryService;
import com.kai.service.impl.CategoryServiceImpl;
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
@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
		"/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
public class CategoryController extends HttpServlet {

	private final ICategoryService cateService = new CategoryServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String path = req.getRequestURI().substring(req.getContextPath().length());
		req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

		if (path.equals("/admin/category/add")) {
			req.setAttribute("form", new CategoryForm());
			req.getRequestDispatcher("/views/admin/category-add.jsp").include(req, resp);
			return;
		}

		if (path.equals("/admin/category/edit")) {
			int id = parseInt(req.getParameter("id"), 0);
			Category category = cateService.findById(id);
			if (category == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/categories");
				return;
			}
			req.setAttribute("form", toForm(category));
			req.setAttribute("cate", category);
			req.getRequestDispatcher("/views/admin/category-edit.jsp").include(req, resp);
			return;
		}

		req.setAttribute("listcate", cateService.findAll());
		req.getRequestDispatcher("/views/admin/category-list.jsp").include(req, resp);
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
			case "/admin/category/insert" -> insertCategory(req, resp);
			case "/admin/category/update" -> updateCategory(req, resp);
			case "/admin/category/delete" -> deleteCategory(req, resp);
			default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void insertCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		CategoryForm form = bindForm(req);
		Map<String, String> errors = ValidationUtil.validate(form);

		if (cateService.findByCategoryname(form.getCategoryname()) != null) {
			errors.put("categoryname", "Tên danh mục đã tồn tại");
		}

		String savedImage = null;
		try {
			savedImage = saveImageIfAny(req);
		} catch (Exception e) {
			errors.put("imageFile", e.getMessage());
		}

		if (!errors.isEmpty()) {
			render(req, resp, form, errors, "/views/admin/category-add.jsp", null);
			return;
		}

		Category category = new Category();
		category.setCategoryname(form.getCategoryname());
		category.setStatus(form.getStatus());
		category.setImages(pickImage(savedImage, form.getImages(), "avatar.png"));

		cateService.insert(category);
		resp.sendRedirect(req.getContextPath() + "/admin/categories");
	}

	private void updateCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		CategoryForm form = bindForm(req);
		Category category = cateService.findById(form.getCategoryid());
		if (category == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/categories");
			return;
		}

		Map<String, String> errors = ValidationUtil.validate(form);

		Category sameName = cateService.findByCategoryname(form.getCategoryname());
		if (sameName != null && sameName.getCategoryid() != category.getCategoryid()) {
			errors.put("categoryname", "Tên danh mục đã tồn tại");
		}

		String savedImage = null;
		try {
			savedImage = saveImageIfAny(req);
		} catch (Exception e) {
			errors.put("imageFile", e.getMessage());
		}

		if (!errors.isEmpty()) {
			render(req, resp, form, errors, "/views/admin/category-edit.jsp", category);
			return;
		}

		String oldImage = category.getImages();
		category.setCategoryname(form.getCategoryname());
		category.setStatus(form.getStatus());
		category.setImages(pickImage(savedImage, form.getImages(), oldImage));

		cateService.update(category);

		if (savedImage != null && oldImage != null && !oldImage.equals(savedImage)) {
			UploadUtil.deleteQuietly(oldImage);
		}
		resp.sendRedirect(req.getContextPath() + "/admin/categories");
	}

	private void deleteCategory(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		int id = parseInt(req.getParameter("id"), 0);
		Category category = cateService.findById(id);
		if (category != null) {
			String image = category.getImages();
			try {
				cateService.delete(id);
				UploadUtil.deleteQuietly(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resp.sendRedirect(req.getContextPath() + "/admin/categories");
	}

	private CategoryForm bindForm(HttpServletRequest req) {
		CategoryForm form = new CategoryForm();
		form.setCategoryid(parseInt(req.getParameter("categoryid"), 0));
		form.setCategoryname(trim(req.getParameter("categoryname")));
		form.setImages(trim(req.getParameter("images")));
		form.setStatus("1".equals(req.getParameter("status")) ? 1 : 0);
		return form;
	}

	private CategoryForm toForm(Category category) {
		CategoryForm form = new CategoryForm();
		form.setCategoryid(category.getCategoryid());
		form.setCategoryname(category.getCategoryname());
		form.setImages(category.getImages());
		form.setStatus(category.getStatus());
		return form;
	}

	private String pickImage(String savedImage, String linkImage, String fallback) {
		if (savedImage != null) {
			return savedImage;
		}
		if (linkImage != null && !linkImage.isBlank()) {
			return linkImage.trim();
		}
		return fallback;
	}

	private String saveImageIfAny(HttpServletRequest req) throws Exception {
		Part part = req.getPart("images1");
		if (part == null || part.getSize() == 0) {
			return null;
		}
		return UploadUtil.saveImage(part);
	}

	private void render(HttpServletRequest req, HttpServletResponse resp, CategoryForm form,
			Map<String, String> errors, String view, Category category)
			throws ServletException, IOException {

		req.setAttribute("form", form);
		req.setAttribute("errors", errors);
		req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
		if (category != null) {
			req.setAttribute("cate", category);
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
}
