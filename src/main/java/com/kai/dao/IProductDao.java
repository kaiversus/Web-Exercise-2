package com.kai.dao;

import java.util.List;

import com.kai.entity.Product;

public interface IProductDao {

	void insert(Product product);

	void update(Product product);

	void delete(int productid) throws Exception;

	Product findById(int productid);

	List<Product> findAll();

	List<Product> findNewest(int limit);

	List<Product> findByPage(int page, int pagesize);

	List<Product> searchByName(String keyword);

	int count();

	Product findActiveById(int productid);

	List<Product> findActiveNewest(int limit);

	List<Product> findActiveByPage(int page, int pagesize);

	int countActive();
}
