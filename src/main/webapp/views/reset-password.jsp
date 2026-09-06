<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đặt lại mật khẩu</title>
</head>
<body>

	<h2>Đặt lại mật khẩu</h2>
	<p>Nếu email vừa nhập tồn tại trong hệ thống, một mã OTP gồm 6 chữ số đã được gửi tới
		hộp thư đó. Mã có hiệu lực trong 5 phút.</p>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>

	<form action="<c:url value='/reset-password'/>" method="post">

		<label>Mã OTP</label><br>
		<input type="text" name="otp" inputmode="numeric" pattern="[0-9]{6}"
			maxlength="6" required autocomplete="one-time-code"
			style="font-size:22px;letter-spacing:6px;width:170px"><br>

		<label>Mật khẩu mới (tối thiểu 8 ký tự, có chữ và số)</label><br>
		<input type="password" name="password" minlength="8" required
			autocomplete="new-password"><br>

		<label>Xác nhận mật khẩu mới</label><br>
		<input type="password" name="confirmPassword" minlength="8" required
			autocomplete="new-password"><br><br>

		<button type="submit">Đổi mật khẩu</button>
	</form>

	<p><a href="<c:url value='/forgot-password'/>">Gửi lại mã</a></p>

</body>
</html>
