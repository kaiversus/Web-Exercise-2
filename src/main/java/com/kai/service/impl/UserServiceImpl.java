package com.kai.service.impl;

import java.time.LocalDateTime;

import com.kai.dao.IUserDao;
import com.kai.dao.impl.UserDao;
import com.kai.entity.User;
import com.kai.service.IUserService;
import com.kai.service.OtpResult;
import com.kai.util.AppConfig;
import com.kai.util.Constant;
import com.kai.util.EmailUtil;
import com.kai.util.OtpUtil;
import com.kai.util.PasswordUtil;

public class UserServiceImpl implements IUserService {

	private final IUserDao userDao = new UserDao();

	private static final int OTP_LENGTH = AppConfig.getInt("otp.length", 6);
	private static final int OTP_TTL_MIN = AppConfig.getInt("otp.ttl.minutes", 5);
	private static final int OTP_MAX_TRY = AppConfig.getInt("otp.max.attempt", 5);

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return userDao.findByUsername(username) != null;
	}

	@Override
	public boolean isEmailTaken(String email) {
		return userDao.findByEmail(email) != null;
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}

	@Override
	public void register(User user, String rawPassword) throws Exception {
		user.setPassword(PasswordUtil.hash(rawPassword));
		user.setRole(Constant.ROLE_USER);
		user.setStatus(Constant.STATUS_INACTIVE);
		user.setCreatedDate(LocalDateTime.now());

		String otp = OtpUtil.generate(OTP_LENGTH);
		user.setOtpHash(PasswordUtil.hash(otp));
		user.setOtpPurpose("ACTIVATE");
		user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(OTP_TTL_MIN));
		user.setOtpAttempt(0);

		if (user.getId() == 0) {
			userDao.insert(user);
		} else {
			userDao.update(user);
		}

		EmailUtil.sendOtp(user.getEmail(), user.getFullname(), otp,
				OTP_TTL_MIN, "kích hoạt tài khoản");
	}

	@Override
	public void issueOtp(User user, String purpose) throws Exception {
		String otp = OtpUtil.generate(OTP_LENGTH);
		user.setOtpHash(PasswordUtil.hash(otp));
		user.setOtpPurpose(purpose);
		user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(OTP_TTL_MIN));
		user.setOtpAttempt(0);
		userDao.update(user);

		String text = "RESET".equals(purpose) ? "đặt lại mật khẩu" : "kích hoạt tài khoản";
		EmailUtil.sendOtp(user.getEmail(), user.getFullname(), otp, OTP_TTL_MIN, text);
	}

	@Override
	public OtpResult checkOtp(User user, String otpInput, String purpose) {
		if (user == null || user.getOtpHash() == null) {
			return OtpResult.NOT_FOUND;
		}
		if (purpose == null || !purpose.equals(user.getOtpPurpose())) {
			return OtpResult.NOT_FOUND;
		}
		if (user.getOtpAttempt() >= OTP_MAX_TRY) {
			return OtpResult.LOCKED;
		}
		if (user.getOtpExpiredAt() == null
				|| LocalDateTime.now().isAfter(user.getOtpExpiredAt())) {
			return OtpResult.EXPIRED;
		}

		if (!PasswordUtil.verify(otpInput, user.getOtpHash())) {
			user.setOtpAttempt(user.getOtpAttempt() + 1);
			userDao.update(user);
			return OtpResult.WRONG;
		}

		user.setOtpHash(null);
		user.setOtpPurpose(null);
		user.setOtpExpiredAt(null);
		user.setOtpAttempt(0);
		userDao.update(user);
		return OtpResult.OK;
	}
}
