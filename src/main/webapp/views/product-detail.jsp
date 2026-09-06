<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>
	<c:choose>
		<c:when test="${not empty product}"><c:out value="${product.productname}"/></c:when>
		<c:otherwise>Không tìm thấy sản phẩm</c:otherwise>
	</c:choose>
</title>
</head>
<body>

<c:if test="${not empty error}">
	<div class="text-center py-5">
		<i class="bi bi-exclamation-triangle display-3 text-warning"></i>
		<h4 class="mt-3"><c:out value="${error}"/></h4>
		<a href="<c:url value='/product'/>" class="btn btn-primary mt-2">
			<i class="bi bi-arrow-left me-1"></i>Quay lại danh sách
		</a>
	</div>
</c:if>

<c:if test="${not empty product}">
	<c:set var="img" value="${empty product.images ? 'avatar.png' : product.images}"/>
	<c:choose>
		<c:when test="${fn:startsWith(img, 'http')}">
			<c:set var="imgUrl" value="${img}"/>
		</c:when>
		<c:otherwise>
			<c:url value="/image" var="imgUrl">
				<c:param name="fname" value="${img}"/>
			</c:url>
		</c:otherwise>
	</c:choose>

	<nav aria-label="breadcrumb">
		<ol class="breadcrumb">
			<li class="breadcrumb-item"><a href="<c:url value='/home'/>">Trang chủ</a></li>
			<li class="breadcrumb-item"><a href="<c:url value='/product'/>">Sản phẩm</a></li>
			<li class="breadcrumb-item active" aria-current="page">
				<c:out value="${product.productname}"/>
			</li>
		</ol>
	</nav>

	<div class="card border-0 shadow-sm">
		<div class="card-body p-4">
			<div class="row g-4">

				<div class="col-md-5">
					<img src="${imgUrl}" class="img-fluid rounded border w-100"
						style="object-fit:cover" alt="<c:out value='${product.productname}'/>">
				</div>

				<div class="col-md-7">
					<h3><c:out value="${product.productname}"/></h3>

					<p class="mb-2">
						<span class="badge text-bg-light border">
							<i class="bi bi-tag me-1"></i><c:out value="${product.category.categoryname}"/>
						</span>
					</p>

					<p class="text-danger fw-bold" style="font-size:1.8rem">
						<fmt:formatNumber value="${product.price}" type="number"/> đ
					</p>

					<p>
						<c:choose>
							<c:when test="${product.quantity > 0}">
								<span class="badge text-bg-success">
									Còn hàng: ${product.quantity} sản phẩm
								</span>
							</c:when>
							<c:otherwise>
								<span class="badge text-bg-secondary">Tạm hết hàng</span>
							</c:otherwise>
						</c:choose>
					</p>

					<p class="text-secondary small mb-4">
						<i class="bi bi-clock me-1"></i>Ngày đăng: ${product.createdDate}
					</p>

					<h6 class="fw-bold">Mô tả sản phẩm</h6>
					<div class="desc-block"><c:out value="${product.description}"/></div>
				</div>

			</div>
		</div>
	</div>

	<a href="<c:url value='/product'/>" class="btn btn-outline-secondary mt-4">
		<i class="bi bi-arrow-left me-1"></i>Quay lại danh sách sản phẩm
	</a>
</c:if>

</body>
</html>
