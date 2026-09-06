<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng ký tài khoản</title>
</head>
<body>

	<h2>Đăng ký tài khoản</h2>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

	<form action="<c:url value='/register'/>" method="post">

		<label>Tên đăng nhập *</label><br>
		<input type="text" name="username" value="<c:out value='${username}'/>"
			pattern="[a-zA-Z0-9_]{4,30}" required><br>

		<label>Email *</label><br>
		<input type="email" name="email" value="<c:out value='${email}'/>" required><br>

		<label>Họ và tên</label><br>
		<input type="text" name="fullname" value="<c:out value='${fullname}'/>"><br>

		<label>Số điện thoại</label><br>
		<input type="text" name="phone" value="<c:out value='${phone}'/>"
			pattern="0[0-9]{9}"><br>

		<label>Mật khẩu * (tối thiểu 8 ký tự, có chữ và số)</label><br>
		<input type="password" name="password" minlength="8" required
			autocomplete="new-password"><br>

		<label>Xác nhận mật khẩu *</label><br>
		<input type="password" name="confirmPassword" minlength="8" required
			autocomplete="new-password"><br><br>

		<button type="submit">Đăng ký</button>
		<a href="<c:url value='/login'/>">Đã có tài khoản? Đăng nhập</a>
	</form>

</body>
</html>
