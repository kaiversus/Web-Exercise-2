package com.kai.service.impl;

import java.util.List;

import com.kai.dao.ICategoryDao;
import com.kai.dao.impl.CategoryDao;
import com.kai.entity.Category;
import com.kai.service.ICategoryService;

public class CategoryServiceImpl implements ICategoryService {

	private ICategoryDao cateDao = new CategoryDao();

	@Override
	public void insert(Category category) {
		// chỉ thêm khi chưa tồn tại tên này
		Category cate = this.findByCategoryname(category.getCategoryname());
		if (cate == null) {
			cateDao.insert(category);
		}
	}

	@Override
	public void update(Category category) {
		Category cate = this.findById(category.getCategoryid());
		if (cate != null) {
			cateDao.update(category);
		}
	}

	@Override
	public void delete(int cateid) {
		try {
			cateDao.delete(cateid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Category findById(int cateid) {
		return cateDao.findById(cateid);
	}

	@Override
	public Category findByCategoryname(String name) {
		return cateDao.findByCategoryname(name);
	}

	@Override
	public List<Category> findAll() {
		return cateDao.findAll();
	}

	@Override
	public List<Category> findAll(int page, int pagesize) {
		return cateDao.findAll(page, pagesize);
	}

	@Override
	public List<Category> searchByName(String catname) {
		return cateDao.searchByName(catname);
	}

	@Override
	public int count() {
		return cateDao.count();
	}
}