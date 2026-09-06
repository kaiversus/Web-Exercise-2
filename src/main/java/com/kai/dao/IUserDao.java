package com.kai.dao;

import com.kai.entity.User;

public interface IUserDao {

	void insert(User user);

	void update(User user);

	User findById(int id);

	User findByUsername(String username);

	User findByEmail(String email);
}
