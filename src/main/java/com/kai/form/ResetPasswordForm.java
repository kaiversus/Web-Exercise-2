package com.kai.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetPasswordForm {

	@NotBlank(message = "Vui lòng nhập mã OTP")
	@Pattern(regexp = "^[0-9]{6}$", message = "Mã OTP gồm đúng 6 chữ số")
	private String otp;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 8, max = 72, message = "Mật khẩu từ 8 đến 72 ký tự")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).+$",
			message = "Mật khẩu phải có cả chữ và số")
	private String password;

	@NotBlank(message = "Vui lòng nhập lại mật khẩu")
	private String confirmPassword;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean passwordMatches() {
		return password != null && password.equals(confirmPassword);
	}
}
