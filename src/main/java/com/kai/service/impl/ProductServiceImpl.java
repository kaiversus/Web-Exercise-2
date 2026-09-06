package com.kai.service.impl;

import java.util.List;

import com.kai.dao.IProductDao;
import com.kai.dao.impl.ProductDao;
import com.kai.entity.Product;
import com.kai.service.IProductService;

public class ProductServiceImpl implements IProductService {

	private final IProductDao productDao = new ProductDao();

	@Override
	public void insert(Product product) {
		productDao.insert(product);
	}

	@Override
	public void update(Product product) {
		productDao.update(product);
	}

	@Override
	public void delete(int productid) throws Exception {
		productDao.delete(productid);
	}

	@Override
	public Product findById(int productid) {
		return productDao.findById(productid);
	}

	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public List<Product> findNewest(int limit) {
		return productDao.findNewest(limit);
	}

	@Override
	public List<Product> findByPage(int page, int pagesize) {
		return productDao.findByPage(page, pagesize);
	}

	@Override
	public List<Product> searchByName(String keyword) {
		return productDao.searchByName(keyword);
	}

	@Override
	public int count() {
		return productDao.count();
	}

	@Override
	public int totalPages(int pagesize) {
		int total = productDao.count();
		int size = Math.max(pagesize, 1);
		return (total + size - 1) / size;
	}

	@Override
	public Product findActiveById(int productid) {
		return productDao.findActiveById(productid);
	}

	@Override
	public List<Product> findActiveNewest(int limit) {
		return productDao.findActiveNewest(limit);
	}

	@Override
	public List<Product> findActiveByPage(int page, int pagesize) {
		return productDao.findActiveByPage(page, pagesize);
	}

	@Override
	public int countActive() {
		return productDao.countActive();
	}

	@Override
	public int totalActivePages(int pagesize) {
		int total = productDao.countActive();
		int size = Math.max(pagesize, 1);
		return (total + size - 1) / size;
	}
}
