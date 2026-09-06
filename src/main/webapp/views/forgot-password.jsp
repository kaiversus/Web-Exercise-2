<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quên mật khẩu</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-5 col-md-7">
		<div class="card shadow-sm border-0">
			<div class="card-body p-4">

				<h3 class="card-title mb-4">Quên mật khẩu</h3>

				<c:if test="${not empty error}">
					<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
				</c:if>

				<form action="<c:url value='/forgot-password'/>" method="post" novalidate>
					<div class="mb-4">
						<label class="form-label">Email</label>
						<input type="email" name="email" maxlength="150" required autocomplete="email"
							class="form-control ${not empty errors.email ? 'is-invalid' : ''}"
							value="<c:out value='${form.email}'/>">
						<c:if test="${not empty errors.email}">
							<div class="invalid-feedback d-block"><c:out value="${errors.email}"/></div>
						</c:if>
					</div>

					<button type="submit" class="btn btn-primary w-100">
						<i class="bi bi-send me-1"></i>Gửi mã OTP
					</button>
				</form>

				<hr class="my-4">
				<p class="text-center mb-0 small">
					<a href="<c:url value='/login'/>">Quay lại đăng nhập</a>
				</p>

			</div>
		</div>
	</div>
</div>

</body>
</html>
