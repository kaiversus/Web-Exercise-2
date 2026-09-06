<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý Sản phẩm</title>
</head>
<body>

	<h2>Quản lý Sản phẩm</h2>
	<a href="<c:url value='/admin/product/add'/>">Thêm sản phẩm</a> |
	<a href="<c:url value='/admin/categories'/>">Quản lý Category</a>
	<hr>

	<table border="1" width="100%" cellpadding="6">
		<tr>
			<th>STT</th>
			<th>Ảnh</th>
			<th>Tên sản phẩm</th>
			<th>Danh mục</th>
			<th>Giá</th>
			<th>SL</th>
			<th>Trạng thái</th>
			<th>Thao tác</th>
		</tr>

		<c:forEach items="${listproduct}" var="p" varStatus="stt">
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

			<tr>
				<td>${stt.index + 1}</td>
				<td><img src="${imgUrl}" width="90" height="70"></td>
				<td><c:out value="${p.productname}"/></td>
				<td><c:out value="${p.category.categoryname}"/></td>
				<td><fmt:formatNumber value="${p.price}" type="number"/> đ</td>
				<td>${p.quantity}</td>
				<td>
					<c:choose>
						<c:when test="${p.status == 1}">Hiển thị</c:when>
						<c:otherwise>Ẩn</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a href="<c:url value='/admin/product/edit'/>?id=${p.productid}">Sửa</a>
					<form action="<c:url value='/admin/product/delete'/>" method="post"
						style="display:inline"
						onsubmit="return confirm('Bạn chắc chắn muốn xoá?')">
						<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
						<input type="hidden" name="id" value="${p.productid}">
						<button type="submit">Xoá</button>
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>
