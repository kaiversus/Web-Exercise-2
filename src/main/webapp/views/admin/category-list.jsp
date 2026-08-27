<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh sách Category</title>
</head>
<body>

	<h2>Quản lý Category</h2>
	<a href="<c:url value='/admin/category/add'/>">Add Category</a><br>
	<hr>

	<table border="1" width="100%">
		<tr>
			<th>STT</th>
			<th>Images</th>
			<th>Category name</th>
			<th>Status</th>
			<th>Action</th>
		</tr>

		<c:forEach items="${listcate}" var="cate" varStatus="STT">
			<c:set var="img" value="${empty cate.images ? 'avatar.png' : cate.images}" />
			<c:choose>
				<c:when test="${fn:startsWith(img, 'http')}">
					<c:set var="imgUrl" value="${img}" />
				</c:when>
				<c:otherwise>
					<c:url value="/image" var="imgUrl">
						<c:param name="fname" value="${img}" />
					</c:url>
				</c:otherwise>
			</c:choose>

			<tr>
				<td>${STT.index + 1}</td>
				<td><img height="150" width="200" src="${imgUrl}" /></td>
				<td>${cate.categoryname}</td>
				<td>
					<c:choose>
						<c:when test="${cate.status == 1}">Hoạt động</c:when>
						<c:otherwise>Khóa</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>">Sửa</a> |
					<a href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>"
					   onclick="return confirm('Bạn chắc chắn muốn xóa?')">Xóa</a>
				</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>