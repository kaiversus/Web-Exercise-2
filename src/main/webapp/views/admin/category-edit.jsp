<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa Category</title>
</head>
<body>

	<h2>Sửa Category</h2>

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

	<form action="<c:url value='/admin/category/update'/>" method="post"
		enctype="multipart/form-data">

		<input type="hidden" name="categoryid" value="${cate.categoryid}">

		<label for="categoryname">Category name:</label><br>
		<input type="text" id="categoryname" name="categoryname"
			value="${cate.categoryname}" required><br>

		<label for="images">Link images:</label><br>
		<input type="text" id="images" name="images" value="${cate.images}"><br>

		<img height="150" width="200" src="${imgUrl}" /><br>

		<label for="images1">Upload images:</label><br>
		<input type="file" id="images1" name="images1" accept="image/*"><br>

		<label>Status</label><br>
		<input type="radio" id="ston" name="status" value="1"
			${cate.status == 1 ? 'checked' : ''}>
		<label for="ston">Hoạt động</label><br>
		<input type="radio" id="stoff" name="status" value="0"
			${cate.status != 1 ? 'checked' : ''}>
		<label for="stoff">Khóa</label>

		<br><br>
		<input type="submit" value="Update">
		<a href="<c:url value='/admin/categories'/>">Quay lại</a>
	</form>

</body>
</html>