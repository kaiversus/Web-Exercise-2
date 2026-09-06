<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm danh mục</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-8">

		<h3 class="mb-3"><i class="bi bi-plus-square me-2"></i>Thêm danh mục</h3>

		<c:if test="${not empty error}">
			<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
		</c:if>

		<div class="card border-0 shadow-sm">
			<div class="card-body p-4">
				<form action="<c:url value='/admin/category/insert'/>" method="post"
					enctype="multipart/form-data" novalidate>

					<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">

					<div class="mb-3">
						<label class="form-label">Tên danh mục <span class="text-danger">*</span></label>
						<input type="text" name="categoryname" maxlength="255" required
							class="form-control ${not empty errors.categoryname ? 'is-invalid' : ''}"
							value="<c:out value='${form.categoryname}'/>">
						<c:if test="${not empty errors.categoryname}">
							<div class="invalid-feedback d-block"><c:out value="${errors.categoryname}"/></div>
						</c:if>
					</div>

					<div class="mb-3">
						<label class="form-label">Đường dẫn ảnh (nếu dùng ảnh ngoài)</label>
						<input type="text" name="images" maxlength="500"
							class="form-control ${not empty errors.images ? 'is-invalid' : ''}"
							value="<c:out value='${form.images}'/>">
						<c:if test="${not empty errors.images}">
							<div class="invalid-feedback d-block"><c:out value="${errors.images}"/></div>
						</c:if>
					</div>

					<div class="mb-3">
						<label class="form-label">Hoặc tải ảnh lên</label>
						<input type="file" name="images1" accept="image/*"
							class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.imageFile}">
							<div class="invalid-feedback d-block"><c:out value="${errors.imageFile}"/></div>
						</c:if>
						<div class="form-text">Nếu tải ảnh lên, ảnh này sẽ được ưu tiên.</div>
					</div>

					<div class="mb-4">
						<label class="form-label d-block">Trạng thái</label>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="status" id="st1" value="1"
								${form.status != 0 ? 'checked' : ''}>
							<label class="form-check-label" for="st1">Hoạt động</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="status" id="st0" value="0"
								${form.status == 0 ? 'checked' : ''}>
							<label class="form-check-label" for="st0">Khóa</label>
						</div>
					</div>

					<button type="submit" class="btn btn-primary">
						<i class="bi bi-check-lg me-1"></i>Thêm mới
					</button>
					<a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary">Quay lại</a>
				</form>
			</div>
		</div>

	</div>
</div>

</body>
</html>
