<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Xác thực tài khoản</title>
</head>
<body>

	<h2>Nhập mã OTP</h2>
	<p>Mã gồm 6 chữ số đã được gửi tới email của tài khoản
		<b><c:out value="${username}"/></b>. Mã có hiệu lực 5 phút.</p>

	<c:if test="${not empty error}">
		<p style="color:red"><c:out value="${error}"/></p>
	</c:if>
	<c:if test="${not empty message}">
		<p style="color:green"><c:out value="${message}"/></p>
	</c:if>

	<form action="<c:url value='/verify'/>" method="post">
		<input type="text" name="otp" inputmode="numeric" pattern="[0-9]{6}"
			maxlength="6" required autocomplete="one-time-code"
			style="font-size:24px;letter-spacing:6px;width:180px">
		<br><br>
		<button type="submit">Xác thực</button>
	</form>

	<p><a href="<c:url value='/verify/resend'/>">Gửi lại mã</a></p>

</body>
</html>
