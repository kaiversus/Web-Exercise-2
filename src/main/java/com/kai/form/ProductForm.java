package com.kai.form;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductForm {

	private int productid;

	@NotBlank(message = "Tên sản phẩm không được để trống")
	@Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
	private String productname;

	@Size(max = 5000, message = "Mô tả tối đa 5000 ký tự")
	private String description;

	@NotNull(message = "Giá không hợp lệ")
	@DecimalMin(value = "0", message = "Giá phải từ 0 trở lên")
	@DecimalMax(value = "999999999999", message = "Giá quá lớn")
	private BigDecimal price;

	@Min(value = 0, message = "Số lượng phải từ 0 trở lên")
	@Max(value = 1000000, message = "Số lượng tối đa 1.000.000")
	private int quantity;

	@Min(value = 1, message = "Vui lòng chọn danh mục")
	private int categoryid;

	private int status;

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
