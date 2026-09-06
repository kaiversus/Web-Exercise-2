package com.kai.dao.impl;

import java.util.List;

import com.kai.config.JPAConfig;
import com.kai.dao.IProductDao;
import com.kai.entity.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class ProductDao implements IProductDao {

	private static final String JPQL_ACTIVE =
			"SELECT p FROM Product p WHERE p.status = 1 "
			+ "ORDER BY p.createdDate DESC, p.productid DESC";

	@Override
	public void insert(Product product) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.persist(product);
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public void update(Product product) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.merge(product);
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public void delete(int productid) throws Exception {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			Product product = enma.find(Product.class, productid);
			if (product == null) {
				throw new Exception("Không tìm thấy product id = " + productid);
			}
			enma.remove(product);
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive()) {
				trans.rollback();
			}
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public Product findById(int productid) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			return enma.find(Product.class, productid);
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> findAll() {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			return enma.createNamedQuery("Product.findAll", Product.class).getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> findNewest(int limit) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query =
					enma.createNamedQuery("Product.findAll", Product.class);
			query.setMaxResults(Math.min(Math.max(limit, 1), 100));
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> findByPage(int page, int pagesize) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			int safePage = Math.max(page, 1);
			int safeSize = Math.min(Math.max(pagesize, 1), 100);
			TypedQuery<Product> query =
					enma.createNamedQuery("Product.findAll", Product.class);
			query.setFirstResult((safePage - 1) * safeSize);
			query.setMaxResults(safeSize);
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> searchByName(String keyword) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT p FROM Product p WHERE p.productname LIKE :kw "
					+ "ORDER BY p.createdDate DESC";
			TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
			query.setParameter("kw", "%" + (keyword == null ? "" : keyword) + "%");
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public int count() {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			Query query = enma.createQuery("SELECT count(p) FROM Product p");
			return ((Long) query.getSingleResult()).intValue();
		} finally {
			enma.close();
		}
	}

	@Override
	public Product findActiveById(int productid) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT p FROM Product p WHERE p.productid = :id AND p.status = 1";
			TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
			query.setParameter("id", productid);
			List<Product> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> findActiveNewest(int limit) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = enma.createQuery(JPQL_ACTIVE, Product.class);
			query.setMaxResults(Math.min(Math.max(limit, 1), 100));
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Product> findActiveByPage(int page, int pagesize) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			int safePage = Math.max(page, 1);
			int safeSize = Math.min(Math.max(pagesize, 1), 100);
			TypedQuery<Product> query = enma.createQuery(JPQL_ACTIVE, Product.class);
			query.setFirstResult((safePage - 1) * safeSize);
			query.setMaxResults(safeSize);
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public int countActive() {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			Query query = enma.createQuery(
					"SELECT count(p) FROM Product p WHERE p.status = 1");
			return ((Long) query.getSingleResult()).intValue();
		} finally {
			enma.close();
		}
	}
}
