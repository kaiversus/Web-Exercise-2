<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chủ</title>
</head>
<body>

<div class="p-4 p-md-5 mb-4 rounded-3 bg-primary text-white shadow-sm">
	<h1 class="display-6 fw-bold mb-3">52HZ-platform</h1>
	<a href="<c:url value='/product'/>" class="btn btn-light">
		Xem tất cả sản phẩm <i class="bi bi-arrow-right ms-1"></i>
	</a>
</div>

<div class="d-flex align-items-center justify-content-between mb-3">
	<h4 class="mb-0"><i class="bi bi-stars text-warning me-1"></i>Sản phẩm mới nhất</h4>
	<a href="<c:url value='/product'/>" class="small">Xem tất cả</a>
</div>

<c:if test="${empty newestProducts}">
	<div class="alert alert-info">Chưa có sản phẩm nào.</div>
</c:if>

<div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-xl-5 g-3">
	<c:forEach items="${newestProducts}" var="p">
		<c:set var="img" value="${empty p.images ? 'avatar.png' : p.images}"/>
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

		<div class="col">
			<a class="card h-100 border-0 shadow-sm product-card text-decoration-none text-body"
				href="<c:url value='/product/detail'/>?id=${p.productid}">
				<img src="${imgUrl}" class="card-img-top product-thumb"
					alt="<c:out value='${p.productname}'/>">
				<div class="card-body">
					<div class="product-title fw-semibold"><c:out value="${p.productname}"/></div>
					<div class="text-secondary small mb-1">
						<c:out value="${p.category.categoryname}"/>
					</div>
					<div class="text-danger fw-bold">
						<fmt:formatNumber value="${p.price}" type="number"/> đ
					</div>
				</div>
			</a>
		</div>
	</c:forEach>
</div>

</body>
</html>
