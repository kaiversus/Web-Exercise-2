<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng ký tài khoản</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-7 col-md-9">
		<div class="card shadow-sm border-0">
			<div class="card-body p-4">

				<h3 class="card-title mb-4">Đăng ký tài khoản</h3>

				<c:if test="${not empty error}">
					<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
				</c:if>

				<form action="<c:url value='/register'/>" method="post" novalidate>

					<div class="mb-3">
						<label class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
						<input type="text" name="username" maxlength="30" required
							class="form-control ${not empty errors.username ? 'is-invalid' : ''}"
							value="<c:out value='${form.username}'/>">
						<c:if test="${not empty errors.username}">
							<div class="invalid-feedback d-block"><c:out value="${errors.username}"/></div>
						</c:if>
						<div class="form-text">4-30 ký tự, chỉ gồm chữ, số và dấu gạch dưới.</div>
					</div>

					<div class="mb-3">
						<label class="form-label">Email <span class="text-danger">*</span></label>
						<input type="email" name="email" maxlength="150" required
							class="form-control ${not empty errors.email ? 'is-invalid' : ''}"
							value="<c:out value='${form.email}'/>">
						<c:if test="${not empty errors.email}">
							<div class="invalid-feedback d-block"><c:out value="${errors.email}"/></div>
						</c:if>
					</div>

					<div class="row">
						<div class="col-md-7 mb-3">
							<label class="form-label">Họ và tên</label>
							<input type="text" name="fullname" maxlength="150"
								class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}"
								value="<c:out value='${form.fullname}'/>">
							<c:if test="${not empty errors.fullname}">
								<div class="invalid-feedback d-block"><c:out value="${errors.fullname}"/></div>
							</c:if>
						</div>
						<div class="col-md-5 mb-3">
							<label class="form-label">Số điện thoại</label>
							<input type="text" name="phone" maxlength="10"
								class="form-control ${not empty errors.phone ? 'is-invalid' : ''}"
								value="<c:out value='${form.phone}'/>">
							<c:if test="${not empty errors.phone}">
								<div class="invalid-feedback d-block"><c:out value="${errors.phone}"/></div>
							</c:if>
						</div>
					</div>

					<div class="mb-3">
						<label class="form-label">Mật khẩu <span class="text-danger">*</span></label>
						<input type="password" name="password" required autocomplete="new-password"
							class="form-control ${not empty errors.password ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.password}">
							<div class="invalid-feedback d-block"><c:out value="${errors.password}"/></div>
						</c:if>
						<div class="form-text">Tối thiểu 8 ký tự, phải có cả chữ và số.</div>
					</div>

					<div class="mb-4">
						<label class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
						<input type="password" name="confirmPassword" required autocomplete="new-password"
							class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.confirmPassword}">
							<div class="invalid-feedback d-block"><c:out value="${errors.confirmPassword}"/></div>
						</c:if>
					</div>

					<button type="submit" class="btn btn-primary w-100">
						<i class="bi bi-person-plus me-1"></i>Đăng ký
					</button>
				</form>

				<hr class="my-4">
				<p class="text-center mb-0 small">
					Đã có tài khoản?
					<a href="<c:url value='/login'/>">Đăng nhập</a>
				</p>

			</div>
		</div>
	</div>
</div>

</body>
</html>
