<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chủ</title>
<style>
.grid { display: flex; flex-wrap: wrap; gap: 16px; }
.card { width: 200px; border: 1px solid #ddd; border-radius: 6px; padding: 10px;
        text-decoration: none; color: #222; }
.card:hover { box-shadow: 0 2px 8px rgba(0,0,0,.2); }
.card img { width: 100%; height: 150px; object-fit: cover; border-radius: 4px; }
.name { font-weight: bold; margin: 8px 0 4px; min-height: 38px; }
.price { color: #c62828; font-weight: bold; }
.cate { color: #888; font-size: 13px; }
</style>
</head>
<body>

	<h1>Sản phẩm mới nhất</h1>

	<c:if test="${empty newestProducts}">
		<p>Chưa có sản phẩm nào.</p>
	</c:if>

	<div class="grid">
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

			<a class="card" href="<c:url value='/product/detail'/>?id=${p.productid}">
				<img src="${imgUrl}" alt="<c:out value='${p.productname}'/>">
				<div class="name"><c:out value="${p.productname}"/></div>
				<div class="cate"><c:out value="${p.category.categoryname}"/></div>
				<div class="price"><fmt:formatNumber value="${p.price}" type="number"/> đ</div>
			</a>
		</c:forEach>
	</div>

	<p style="margin-top:20px">
		<a href="<c:url value='/product'/>">Xem tất cả sản phẩm &rarr;</a>
	</p>

</body>
</html>
