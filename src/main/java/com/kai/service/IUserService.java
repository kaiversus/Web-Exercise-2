package com.kai.service;

import com.kai.entity.User;

public interface IUserService {

	User findByUsername(String username);

	User findByEmail(String email);

	User findById(int id);

	boolean isUsernameTaken(String username);

	boolean isEmailTaken(String email);

	void update(User user);

	void register(User user, String rawPassword) throws Exception;

	void issueOtp(User user, String purpose) throws Exception;

	OtpResult checkOtp(User user, String otpInput, String purpose);
}
