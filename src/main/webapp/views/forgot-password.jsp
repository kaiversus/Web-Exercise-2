<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quên mật khẩu</title>
</head>
<body>

	<h2>Quên mật khẩu</h2>
	<p>Nhập email bạn đã dùng để đăng ký. Chúng tôi sẽ gửi mã OTP để đặt lại mật khẩu.</p>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

	<form action="<c:url value='/forgot-password'/>" method="post">
		<label>Email</label><br>
		<input type="email" name="email" required autocomplete="email"><br><br>
		<button type="submit">Gửi mã OTP</button>
	</form>

	<p><a href="<c:url value='/login'/>">Quay lại đăng nhập</a></p>

</body>
</html>
