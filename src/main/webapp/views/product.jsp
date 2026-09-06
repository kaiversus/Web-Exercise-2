<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tất cả sản phẩm</title>
</head>
<body>

<div class="d-flex flex-wrap align-items-center justify-content-between mb-3">
	<h3 class="mb-0">Tất cả sản phẩm</h3>
	<span class="text-secondary small">
		${totalItems} sản phẩm / trang ${currentPage}/${totalPages}
	</span>
</div>

<c:if test="${empty products}">
	<div class="alert alert-info">Chưa có sản phẩm nào.</div>
</c:if>

<div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-4">
	<c:forEach items="${products}" var="p">
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
					<div class="text-secondary small mb-2">
						<i class="bi bi-tag me-1"></i><c:out value="${p.category.categoryname}"/>
					</div>
					<div class="d-flex align-items-center justify-content-between">
						<span class="text-danger fw-bold fs-5">
							<fmt:formatNumber value="${p.price}" type="number"/> đ
						</span>
						<c:choose>
							<c:when test="${p.quantity > 0}">
								<span class="badge text-bg-success">Còn hàng</span>
							</c:when>
							<c:otherwise>
								<span class="badge text-bg-secondary">Hết hàng</span>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</a>
		</div>
	</c:forEach>
</div>

<c:if test="${totalPages > 1}">
	<nav class="mt-4">
		<ul class="pagination justify-content-center">
			<li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
				<a class="page-link" href="<c:url value='/product'/>?page=${currentPage - 1}">
					<i class="bi bi-chevron-left"></i>
				</a>
			</li>
			<c:forEach begin="1" end="${totalPages}" var="i">
				<li class="page-item ${i == currentPage ? 'active' : ''}">
					<a class="page-link" href="<c:url value='/product'/>?page=${i}">${i}</a>
				</li>
			</c:forEach>
			<li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
				<a class="page-link" href="<c:url value='/product'/>?page=${currentPage + 1}">
					<i class="bi bi-chevron-right"></i>
				</a>
			</li>
		</ul>
	</nav>
</c:if>

</body>
</html>
