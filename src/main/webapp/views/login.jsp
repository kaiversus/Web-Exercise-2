<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-5 col-md-7">
		<div class="card shadow-sm border-0">
			<div class="card-body p-4">

				<h3 class="card-title mb-4">Đăng nhập</h3>

				<c:if test="${not empty message}">
					<div class="alert alert-success py-2"><c:out value="${message}"/></div>
				</c:if>
				<c:if test="${not empty error}">
					<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
				</c:if>

				<form action="<c:url value='/login'/>" method="post" novalidate>
					<input type="hidden" name="redirect" value="<c:out value='${redirect}'/>">

					<div class="mb-3">
						<label class="form-label">Tên đăng nhập hoặc Email</label>
						<input type="text" name="account" required autocomplete="username"
							class="form-control ${not empty errors.account ? 'is-invalid' : ''}"
							value="<c:out value='${form.account}'/>">
						<c:if test="${not empty errors.account}">
							<div class="invalid-feedback d-block"><c:out value="${errors.account}"/></div>
						</c:if>
					</div>

					<div class="mb-3">
						<label class="form-label">Mật khẩu</label>
						<input type="password" name="password" required autocomplete="current-password"
							class="form-control ${not empty errors.password ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.password}">
							<div class="invalid-feedback d-block"><c:out value="${errors.password}"/></div>
						</c:if>
					</div>

					<div class="form-check mb-4">
						<input class="form-check-input" type="checkbox" name="remember" id="remember">
						<label class="form-check-label small" for="remember">Ghi nhớ tên đăng nhập</label>
					</div>

					<button type="submit" class="btn btn-primary w-100">
						<i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
					</button>
				</form>

				<hr class="my-4">
				<div class="d-flex justify-content-between small">
					<a href="<c:url value='/forgot-password'/>">Quên mật khẩu?</a>
					<a href="<c:url value='/register'/>">Đăng ký tài khoản</a>
				</div>

			</div>
		</div>
	</div>
</div>

</body>
</html>
