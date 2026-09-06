<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa sản phẩm</title>
</head>
<body>

	<h2>Sửa sản phẩm</h2>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

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

	<form action="<c:url value='/admin/product/update'/>" method="post"
		enctype="multipart/form-data">

		<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
		<input type="hidden" name="productid" value="${product.productid}">

		<label>Tên sản phẩm *</label><br>
		<input type="text" name="productname" maxlength="255" required
			value="<c:out value='${product.productname}'/>"><br>

		<label>Danh mục *</label><br>
		<select name="categoryid" required>
			<c:forEach items="${listcate}" var="cate">
				<option value="${cate.categoryid}"
					${cate.categoryid == product.category.categoryid ? 'selected' : ''}>
					<c:out value="${cate.categoryname}"/>
				</option>
			</c:forEach>
		</select><br>

		<label>Giá (VNĐ) *</label><br>
		<input type="number" name="price" min="0" step="0.01" required
			value="${product.price}"><br>

		<label>Số lượng *</label><br>
		<input type="number" name="quantity" min="0" max="1000000" required
			value="${product.quantity}"><br>

		<label>Mô tả</label><br>
		<textarea name="description" rows="5" cols="60" maxlength="5000"><c:out value="${product.description}"/></textarea><br>

		<label>Ảnh hiện tại</label><br>
		<img src="${imgUrl}" width="180"><br>

		<label>Đổi ảnh mới</label><br>
		<input type="file" name="imageFile" accept="image/*"><br>

		<label>Trạng thái</label><br>
		<input type="radio" name="status" value="1" id="st1"
			${product.status == 1 ? 'checked' : ''}>
		<label for="st1">Hiển thị</label>
		<input type="radio" name="status" value="0" id="st0"
			${product.status != 1 ? 'checked' : ''}>
		<label for="st0">Ẩn</label><br><br>

		<button type="submit">Cập nhật</button>
		<a href="<c:url value='/admin/products'/>">Quay lại</a>
	</form>

</body>
</html>
