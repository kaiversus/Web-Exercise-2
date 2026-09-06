<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm sản phẩm</title>
</head>
<body>

	<h2>Thêm sản phẩm</h2>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

	<form action="<c:url value='/admin/product/insert'/>" method="post"
		enctype="multipart/form-data">

		<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">

		<label>Tên sản phẩm *</label><br>
		<input type="text" name="productname" maxlength="255" required><br>

		<label>Danh mục *</label><br>
		<select name="categoryid" required>
			<option value="">-- Chọn danh mục --</option>
			<c:forEach items="${listcate}" var="cate">
				<option value="${cate.categoryid}"><c:out value="${cate.categoryname}"/></option>
			</c:forEach>
		</select><br>

		<label>Giá (VNĐ) *</label><br>
		<input type="number" name="price" min="0" step="0.01" required><br>

		<label>Số lượng *</label><br>
		<input type="number" name="quantity" min="0" max="1000000" value="0" required><br>

		<label>Mô tả</label><br>
		<textarea name="description" rows="5" cols="60" maxlength="5000"></textarea><br>

		<label>Ảnh sản phẩm</label><br>
		<input type="file" name="imageFile" accept="image/*"><br>

		<label>Trạng thái</label><br>
		<input type="radio" name="status" value="1" id="st1" checked>
		<label for="st1">Hiển thị</label>
		<input type="radio" name="status" value="0" id="st0">
		<label for="st0">Ẩn</label><br><br>

		<button type="submit">Thêm mới</button>
		<a href="<c:url value='/admin/products'/>">Quay lại</a>
	</form>

</body>
</html>
