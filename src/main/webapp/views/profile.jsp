<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang cá nhân</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-8">

		<h3 class="mb-3">Thông tin cá nhân</h3>

		<c:if test="${not empty message}">
			<div class="alert alert-success py-2"><c:out value="${message}"/></div>
		</c:if>
		<c:if test="${not empty error}">
			<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
		</c:if>

		<div class="card shadow-sm border-0">
			<div class="card-body p-4">
				<form action="<c:url value='/profile'/>" method="post"
					enctype="multipart/form-data" novalidate>

					<div class="row">
						<div class="col-md-4 text-center mb-4">
							<c:choose>
								<c:when test="${not empty user.images}">
									<c:url value="/image" var="avatarUrl">
										<c:param name="fname" value="${user.images}"/>
									</c:url>
									<img src="${avatarUrl}" class="rounded-circle border"
										style="width:150px;height:150px;object-fit:cover" alt="Ảnh đại diện">
								</c:when>
								<c:otherwise>
									<i class="bi bi-person-circle text-secondary" style="font-size:150px"></i>
								</c:otherwise>
							</c:choose>

							<div class="mt-3 text-start">
								<label class="form-label small">Đổi ảnh đại diện</label>
								<input type="file" name="images" accept="image/*"
									class="form-control form-control-sm ${not empty errors.images ? 'is-invalid' : ''}">
								<c:if test="${not empty errors.images}">
									<div class="invalid-feedback d-block"><c:out value="${errors.images}"/></div>
								</c:if>
							</div>
						</div>

						<div class="col-md-8">
							<div class="mb-3">
								<label class="form-label">Tên đăng nhập</label>
								<input type="text" class="form-control" readonly disabled
									value="<c:out value='${user.username}'/>">
							</div>

							<div class="mb-3">
								<label class="form-label">Email</label>
								<input type="text" class="form-control" readonly disabled
									value="<c:out value='${user.email}'/>">
							</div>

							<div class="mb-3">
								<label class="form-label">Họ và tên</label>
								<input type="text" name="fullname" maxlength="150"
									class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}"
									value="<c:out value='${form.fullname}'/>">
								<c:if test="${not empty errors.fullname}">
									<div class="invalid-feedback d-block"><c:out value="${errors.fullname}"/></div>
								</c:if>
							</div>

							<div class="mb-4">
								<label class="form-label">Số điện thoại</label>
								<input type="text" name="phone" maxlength="10"
									class="form-control ${not empty errors.phone ? 'is-invalid' : ''}"
									value="<c:out value='${form.phone}'/>">
								<c:if test="${not empty errors.phone}">
									<div class="invalid-feedback d-block"><c:out value="${errors.phone}"/></div>
								</c:if>
							</div>

							<button type="submit" class="btn btn-success">
								<i class="bi bi-check-lg me-1"></i>Cập nhật thông tin
							</button>
						</div>
					</div>

				</form>
			</div>
		</div>

	</div>
</div>

</body>
</html>
