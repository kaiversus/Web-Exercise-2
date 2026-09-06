<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tất cả sản phẩm</title>
<style>
.grid { display: flex; flex-wrap: wrap; gap: 16px; }
.card { width: 220px; border: 1px solid #ddd; border-radius: 6px; padding: 10px;
        text-decoration: none; color: #222; }
.card:hover { box-shadow: 0 2px 8px rgba(0,0,0,.2); }
.card img { width: 100%; height: 160px; object-fit: cover; border-radius: 4px; }
.name { font-weight: bold; margin: 8px 0 4px; min-height: 38px; }
.price { color: #c62828; font-weight: bold; }
.cate { color: #888; font-size: 13px; }
.pager a, .pager span { display: inline-block; padding: 6px 12px; margin: 2px;
                        border: 1px solid #ccc; border-radius: 4px; text-decoration: none; }
.pager .active { background: #2196F3; color: white; border-color: #2196F3; }
.pager .disabled { color: #bbb; border-color: #eee; }
</style>
</head>
<body>

	<h1>Tất cả sản phẩm</h1>
	<p>Tổng cộng ${totalItems} sản phẩm &mdash; trang ${currentPage}/${totalPages}</p>

	<c:if test="${empty products}">
		<p>Chưa có sản phẩm nào.</p>
	</c:if>

	<div class="grid">
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

			<a class="card" href="<c:url value='/product/detail'/>?id=${p.productid}">
				<img src="${imgUrl}" alt="<c:out value='${p.productname}'/>">
				<div class="name"><c:out value="${p.productname}"/></div>
				<div class="cate"><c:out value="${p.category.categoryname}"/></div>
				<div class="price"><fmt:formatNumber value="${p.price}" type="number"/> đ</div>
			</a>
		</c:forEach>
	</div>

	<div class="pager" style="margin-top:24px">
		<c:choose>
			<c:when test="${currentPage > 1}">
				<a href="<c:url value='/product'/>?page=${currentPage - 1}">&laquo; Trước</a>
			</c:when>
			<c:otherwise><span class="disabled">&laquo; Trước</span></c:otherwise>
		</c:choose>

		<c:forEach begin="1" end="${totalPages}" var="i">
			<c:choose>
				<c:when test="${i == currentPage}">
					<span class="active">${i}</span>
				</c:when>
				<c:otherwise>
					<a href="<c:url value='/product'/>?page=${i}">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:choose>
			<c:when test="${currentPage < totalPages}">
				<a href="<c:url value='/product'/>?page=${currentPage + 1}">Sau &raquo;</a>
			</c:when>
			<c:otherwise><span class="disabled">Sau &raquo;</span></c:otherwise>
		</c:choose>
	</div>

</body>
</html>
