<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Xác thực tài khoản</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-5 col-md-7">
		<div class="card shadow-sm border-0">
			<div class="card-body p-4 text-center">

				<i class="bi bi-envelope-check display-4 text-primary"></i>
				<h3 class="card-title mt-2 mb-4">Nhập mã OTP</h3>

				<c:if test="${not empty message}">
					<div class="alert alert-success py-2"><c:out value="${message}"/></div>
				</c:if>
				<c:if test="${not empty error}">
					<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
				</c:if>

				<form action="<c:url value='/verify'/>" method="post" novalidate>
					<div class="mb-3">
						<input type="text" name="otp" inputmode="numeric" maxlength="6" required
							autocomplete="one-time-code"
							class="form-control form-control-lg text-center ${not empty errors.otp ? 'is-invalid' : ''}"
							style="letter-spacing:.6rem; font-weight:600"
							value="<c:out value='${form.otp}'/>">
						<c:if test="${not empty errors.otp}">
							<div class="invalid-feedback d-block"><c:out value="${errors.otp}"/></div>
						</c:if>
					</div>

					<button type="submit" class="btn btn-primary w-100">Xác thực</button>
				</form>

				<hr class="my-4">
				<a class="small" href="<c:url value='/verify/resend'/>">
					<i class="bi bi-arrow-clockwise me-1"></i>Gửi lại mã
				</a>

			</div>
		</div>
	</div>
</div>

</body>
</html>
