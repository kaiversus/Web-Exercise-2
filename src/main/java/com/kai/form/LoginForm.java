package com.kai.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

	@NotBlank(message = "Vui lòng nhập tên đăng nhập hoặc email")
	@Size(max = 150, message = "Giá trị quá dài")
	private String account;

	@NotBlank(message = "Vui lòng nhập mật khẩu")
	@Size(max = 72, message = "Mật khẩu tối đa 72 ký tự")
	private String password;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
