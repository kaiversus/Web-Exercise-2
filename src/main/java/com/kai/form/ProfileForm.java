package com.kai.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProfileForm {

	@Size(max = 150, message = "Họ tên tối đa 150 ký tự")
	private String fullname;

	@Pattern(regexp = "^$|^0[0-9]{9}$",
			message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
	private String phone;

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
}
