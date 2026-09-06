<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
</head>
<body>

	<h2>Đăng nhập</h2>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>
	<c:if test="${not empty message}">
		<p style="color:green"><c:out value="${message}"/></p>
	</c:if>

	<form action="<c:url value='/login'/>" method="post">
		<input type="hidden" name="redirect" value="<c:out value='${redirect}'/>">

		<label>Tên đăng nhập hoặc Email</label><br>
		<input type="text" name="account" required autocomplete="username"
			value="<c:out value='${not empty account ? account : rememberedUsername}'/>"><br>

		<label>Mật khẩu</label><br>
		<input type="password" name="password" required autocomplete="current-password"><br>

		<label><input type="checkbox" name="remember"> Ghi nhớ tên đăng nhập</label><br><br>

		<button type="submit">Đăng nhập</button>
	</form>

	<p>
		<a href="<c:url value='/forgot-password'/>">Quên mật khẩu?</a> |
		<a href="<c:url value='/register'/>">Đăng ký tài khoản</a>
	</p>

</body>
</html>
