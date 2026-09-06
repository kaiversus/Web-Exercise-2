<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đặt lại mật khẩu</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-5 col-md-7">
		<div class="card shadow-sm border-0">
			<div class="card-body p-4">

				<h3 class="card-title mb-4">Đặt lại mật khẩu</h3>

				<c:if test="${not empty error}">
					<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
				</c:if>

				<form action="<c:url value='/reset-password'/>" method="post" novalidate>

					<div class="mb-3">
						<label class="form-label">Mã OTP</label>
						<input type="text" name="otp" inputmode="numeric" maxlength="6" required
							autocomplete="one-time-code"
							class="form-control text-center ${not empty errors.otp ? 'is-invalid' : ''}"
							style="letter-spacing:.5rem; font-weight:600"
							value="<c:out value='${form.otp}'/>">
						<c:if test="${not empty errors.otp}">
							<div class="invalid-feedback d-block"><c:out value="${errors.otp}"/></div>
						</c:if>
					</div>

					<div class="mb-3">
						<label class="form-label">Mật khẩu mới</label>
						<input type="password" name="password" required autocomplete="new-password"
							class="form-control ${not empty errors.password ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.password}">
							<div class="invalid-feedback d-block"><c:out value="${errors.password}"/></div>
						</c:if>
						<div class="form-text">Tối thiểu 8 ký tự, phải có cả chữ và số.</div>
					</div>

					<div class="mb-4">
						<label class="form-label">Xác nhận mật khẩu mới</label>
						<input type="password" name="confirmPassword" required autocomplete="new-password"
							class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.confirmPassword}">
							<div class="invalid-feedback d-block"><c:out value="${errors.confirmPassword}"/></div>
						</c:if>
					</div>

					<button type="submit" class="btn btn-primary w-100">Đổi mật khẩu</button>
				</form>

				<hr class="my-4">
				<p class="text-center mb-0 small">
					<a href="<c:url value='/forgot-password'/>">Gửi lại mã</a>
				</p>

			</div>
		</div>
	</div>
</div>

</body>
</html>
