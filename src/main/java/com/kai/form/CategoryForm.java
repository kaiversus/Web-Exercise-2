package com.kai.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryForm {

	private int categoryid;

	@NotBlank(message = "Tên danh mục không được để trống")
	@Size(max = 255, message = "Tên danh mục tối đa 255 ký tự")
	private String categoryname;

	@Size(max = 500, message = "Đường dẫn ảnh tối đa 500 ký tự")
	private String images;

	private int status;

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
