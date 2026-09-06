package com.kai.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ForgotPasswordForm {

	@NotBlank(message = "Email không được để trống")
	@Size(max = 150, message = "Email tối đa 150 ký tự")
	@Email(regexp = "^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,190}\\.[A-Za-z]{2,}$",
			message = "Email không hợp lệ")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
