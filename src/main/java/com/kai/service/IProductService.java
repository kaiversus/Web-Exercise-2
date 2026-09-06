package com.kai.service;

import java.util.List;

import com.kai.entity.Product;

public interface IProductService {

	void insert(Product product);

	void update(Product product);

	void delete(int productid) throws Exception;

	Product findById(int productid);

	List<Product> findAll();

	List<Product> findNewest(int limit);

	List<Product> findByPage(int page, int pagesize);

	List<Product> searchByName(String keyword);

	int count();

	int totalPages(int pagesize);

	Product findActiveById(int productid);

	List<Product> findActiveNewest(int limit);

	List<Product> findActiveByPage(int page, int pagesize);

	int countActive();

	int totalActivePages(int pagesize);
}
