<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cập nhật Profile</title>
</head>
<body>

	<h2>Thông tin cá nhân</h2>

	<c:if test="${not empty message}">
		<p style="color:green"><c:out value="${message}"/></p>
	</c:if>
	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

	<form action="<c:url value='/profile'/>" method="post" enctype="multipart/form-data">
		<table cellpadding="8">
			<tr>
				<td>Tên đăng nhập:</td>
				<td><input type="text" value="<c:out value='${user.username}'/>" readonly
					style="background:#eee"></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><input type="text" value="<c:out value='${user.email}'/>" readonly
					style="background:#eee"></td>
			</tr>
			<tr>
				<td>Họ và tên:</td>
				<td><input type="text" name="fullname"
					value="<c:out value='${user.fullname}'/>"></td>
			</tr>
			<tr>
				<td>Số điện thoại:</td>
				<td><input type="text" name="phone" pattern="0[0-9]{9}"
					value="<c:out value='${user.phone}'/>"></td>
			</tr>
			<tr>
				<td>Ảnh đại diện:</td>
				<td>
					<c:choose>
						<c:when test="${not empty user.images}">
							<c:url value="/image" var="avatarUrl">
								<c:param name="fname" value="${user.images}"/>
							</c:url>
							<img src="${avatarUrl}" width="120" style="border:1px solid #ccc">
						</c:when>
						<c:otherwise><i>Chưa có ảnh</i></c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>Chọn ảnh mới:</td>
				<td><input type="file" name="images" accept="image/*"></td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="submit" style="padding:10px 20px;background:#4CAF50;
						color:white;border:none;cursor:pointer">Cập nhật thông tin</button>
				</td>
			</tr>
		</table>
	</form>

</body>
</html>
