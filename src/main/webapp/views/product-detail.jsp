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
<style>
.detail { display: flex; gap: 28px; flex-wrap: wrap; }
.detail img { width: 380px; max-width: 100%; border: 1px solid #ddd; border-radius: 6px; }
.info { flex: 1; min-width: 280px; }
.price { color: #c62828; font-size: 26px; font-weight: bold; }
.desc { white-space: pre-wrap; line-height: 1.6; margin-top: 12px; }
</style>
</head>
<body>

	<c:if test="${not empty error}">
		<h2><c:out value="${error}"/></h2>
		<p><a href="<c:url value='/product'/>">&larr; Quay lại danh sách sản phẩm</a></p>
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

		<p>
			<a href="<c:url value='/home'/>">Trang chủ</a> /
			<a href="<c:url value='/product'/>">Sản phẩm</a> /
			<c:out value="${product.productname}"/>
		</p>

		<div class="detail">
			<img src="${imgUrl}" alt="<c:out value='${product.productname}'/>">

			<div class="info">
				<h1><c:out value="${product.productname}"/></h1>
				<p>Danh mục: <c:out value="${product.category.categoryname}"/></p>
				<p class="price"><fmt:formatNumber value="${product.price}" type="number"/> đ</p>
				<p>
					<c:choose>
						<c:when test="${product.quantity > 0}">
							Còn hàng (${product.quantity} sản phẩm)
						</c:when>
						<c:otherwise>Tạm hết hàng</c:otherwise>
					</c:choose>
				</p>
				<p>Ngày đăng: ${product.createdDate}</p>

				<h3>Mô tả sản phẩm</h3>
				<div class="desc"><c:out value="${product.description}"/></div>
			</div>
		</div>

		<p style="margin-top:24px">
			<a href="<c:url value='/product'/>">&larr; Quay lại danh sách sản phẩm</a>
		</p>
	</c:if>

</body>
</html>
