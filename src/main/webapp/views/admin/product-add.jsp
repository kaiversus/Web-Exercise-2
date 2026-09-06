<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm sản phẩm</title>
</head>
<body>

<div class="row justify-content-center">
	<div class="col-lg-9">

		<h3 class="mb-3"><i class="bi bi-plus-square me-2"></i>Thêm sản phẩm</h3>

		<c:if test="${not empty error}">
			<div class="alert alert-danger py-2"><c:out value="${error}"/></div>
		</c:if>

		<div class="card border-0 shadow-sm">
			<div class="card-body p-4">
				<form action="<c:url value='/admin/product/insert'/>" method="post"
					enctype="multipart/form-data" novalidate>

					<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">

					<div class="mb-3">
						<label class="form-label">Tên sản phẩm <span class="text-danger">*</span></label>
						<input type="text" name="productname" maxlength="255" required
							class="form-control ${not empty errors.productname ? 'is-invalid' : ''}"
							value="<c:out value='${form.productname}'/>">
						<c:if test="${not empty errors.productname}">
							<div class="invalid-feedback d-block"><c:out value="${errors.productname}"/></div>
						</c:if>
					</div>

					<div class="row">
						<div class="col-md-6 mb-3">
							<label class="form-label">Danh mục <span class="text-danger">*</span></label>
							<select name="categoryid" required
								class="form-select ${not empty errors.categoryid ? 'is-invalid' : ''}">
								<option value="0">-- Chọn danh mục --</option>
								<c:forEach items="${listcate}" var="cate">
									<option value="${cate.categoryid}"
										${cate.categoryid == form.categoryid ? 'selected' : ''}>
										<c:out value="${cate.categoryname}"/>
									</option>
								</c:forEach>
							</select>
							<c:if test="${not empty errors.categoryid}">
								<div class="invalid-feedback d-block"><c:out value="${errors.categoryid}"/></div>
							</c:if>
						</div>

						<div class="col-md-3 mb-3">
							<label class="form-label">Giá (VNĐ) <span class="text-danger">*</span></label>
							<input type="number" name="price" min="0" step="0.01" required
								class="form-control ${not empty errors.price ? 'is-invalid' : ''}"
								value="${form.price}">
							<c:if test="${not empty errors.price}">
								<div class="invalid-feedback d-block"><c:out value="${errors.price}"/></div>
							</c:if>
						</div>

						<div class="col-md-3 mb-3">
							<label class="form-label">Số lượng <span class="text-danger">*</span></label>
							<input type="number" name="quantity" min="0" max="1000000" required
								class="form-control ${not empty errors.quantity ? 'is-invalid' : ''}"
								value="${form.quantity}">
							<c:if test="${not empty errors.quantity}">
								<div class="invalid-feedback d-block"><c:out value="${errors.quantity}"/></div>
							</c:if>
						</div>
					</div>

					<div class="mb-3">
						<label class="form-label">Mô tả</label>
						<textarea name="description" rows="5" maxlength="5000"
							class="form-control ${not empty errors.description ? 'is-invalid' : ''}"><c:out value="${form.description}"/></textarea>
						<c:if test="${not empty errors.description}">
							<div class="invalid-feedback d-block"><c:out value="${errors.description}"/></div>
						</c:if>
					</div>

					<div class="mb-3">
						<label class="form-label">Ảnh sản phẩm</label>
						<input type="file" name="imageFile" accept="image/*"
							class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}">
						<c:if test="${not empty errors.imageFile}">
							<div class="invalid-feedback d-block"><c:out value="${errors.imageFile}"/></div>
						</c:if>
						<div class="form-text">Chấp nhận jpg, jpeg, png, gif, webp. Tối đa 5MB.</div>
					</div>

					<div class="mb-4">
						<label class="form-label d-block">Trạng thái</label>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="status" id="st1" value="1"
								${form.status != 0 ? 'checked' : ''}>
							<label class="form-check-label" for="st1">Hiển thị</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="status" id="st0" value="0"
								${form.status == 0 ? 'checked' : ''}>
							<label class="form-check-label" for="st0">Ẩn</label>
						</div>
					</div>

					<button type="submit" class="btn btn-primary">
						<i class="bi bi-check-lg me-1"></i>Thêm mới
					</button>
					<a href="<c:url value='/admin/products'/>" class="btn btn-outline-secondary">Quay lại</a>
				</form>
			</div>
		</div>

	</div>
</div>

</body>
</html>
