package com.kai.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterForm {

	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Pattern(regexp = "^[a-zA-Z0-9_]{4,30}$",
			message = "Tên đăng nhập 4-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới")
	private String username;

	@NotBlank(message = "Email không được để trống")
	@Size(max = 150, message = "Email tối đa 150 ký tự")
	@Email(regexp = "^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,190}\\.[A-Za-z]{2,}$",
			message = "Email không hợp lệ")
	private String email;

	@Size(max = 150, message = "Họ tên tối đa 150 ký tự")
	private String fullname;

	@Pattern(regexp = "^$|^0[0-9]{9}$",
			message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
	private String phone;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 8, max = 72, message = "Mật khẩu từ 8 đến 72 ký tự")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).+$",
			message = "Mật khẩu phải có cả chữ và số")
	private String password;

	@NotBlank(message = "Vui lòng nhập lại mật khẩu")
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
