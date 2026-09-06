package com.kai.dao.impl;

import java.util.List;

import com.kai.config.JPAConfig;
import com.kai.dao.IUserDao;
import com.kai.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class UserDao implements IUserDao {

	@Override
	public void insert(User user) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.persist(user);
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
	public void update(User user) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.merge(user);
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
	public User findById(int id) {
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			return enma.find(User.class, id);
		} finally {
			enma.close();
		}
	}

	@Override
	public User findByUsername(String username) {
		if (username == null || username.isBlank()) {
			return null;
		}
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.username = :uname";
			TypedQuery<User> query = enma.createQuery(jpql, User.class);
			query.setParameter("uname", username);
			List<User> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			enma.close();
		}
	}

	@Override
	public User findByEmail(String email) {
		if (email == null || email.isBlank()) {
			return null;
		}
		EntityManager enma = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.email = :mail";
			TypedQuery<User> query = enma.createQuery(jpql, User.class);
			query.setParameter("mail", email);
			List<User> list = query.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			enma.close();
		}
	}
}
