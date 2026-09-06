package com.kai.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(name = "uk_users_username", columnNames = "username"),
		@UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "username", columnDefinition = "varchar(50) not null")
	private String username;

	@Column(name = "email", columnDefinition = "varchar(150) not null")
	private String email;

	@Column(name = "password", columnDefinition = "varchar(100) not null")
	private String password;

	@Column(name = "fullname", columnDefinition = "varchar(150) null")
	private String fullname;

	@Column(name = "phone", columnDefinition = "varchar(20) null")
	private String phone;

	@Column(name = "images", columnDefinition = "varchar(500) null")
	private String images;

	@Column(name = "role")
	private int role;

	@Column(name = "status")
	private int status;

	@Column(name = "otp_hash", columnDefinition = "varchar(100) null")
	private String otpHash;

	@Column(name = "otp_purpose", columnDefinition = "varchar(20) null")
	private String otpPurpose;

	@Column(name = "otp_expired_at")
	private LocalDateTime otpExpiredAt;

	@Column(name = "otp_attempt")
	private int otpAttempt;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOtpHash() {
		return otpHash;
	}

	public void setOtpHash(String otpHash) {
		this.otpHash = otpHash;
	}

	public String getOtpPurpose() {
		return otpPurpose;
	}

	public void setOtpPurpose(String otpPurpose) {
		this.otpPurpose = otpPurpose;
	}

	public LocalDateTime getOtpExpiredAt() {
		return otpExpiredAt;
	}

	public void setOtpExpiredAt(LocalDateTime otpExpiredAt) {
		this.otpExpiredAt = otpExpiredAt;
	}

	public int getOtpAttempt() {
		return otpAttempt;
	}

	public void setOtpAttempt(int otpAttempt) {
		this.otpAttempt = otpAttempt;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
}
