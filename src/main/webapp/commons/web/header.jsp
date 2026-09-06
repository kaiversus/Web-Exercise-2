<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<div style="background-color:#2196F3;color:white;padding:15px">
	<div style="float:left">
		<b style="font-size:20px">Hệ Thống Quản Lý</b>
		&nbsp;&nbsp;
		<a href="<c:url value='/home'/>" style="color:white">Trang chủ</a> |
		<a href="<c:url value='/product'/>" style="color:white">Sản phẩm</a>
	</div>
	<div style="float:right">
		<c:choose>
			<c:when test="${not empty sessionScope.account}">
				<c:if test="${sessionScope.account.role == 1}">
					<a href="<c:url value='/admin/products'/>" style="color:white">Quản trị</a> |
				</c:if>
				<a href="<c:url value='/profile'/>" style="color:white">
					<c:out value="${sessionScope.account.username}"/>
				</a> |
				<a href="<c:url value='/logout'/>" style="color:white">Đăng xuất</a>
			</c:when>
			<c:otherwise>
				<a href="<c:url value='/login'/>" style="color:white">Đăng nhập</a> |
				<a href="<c:url value='/register'/>" style="color:white">Đăng ký</a>
			</c:otherwise>
		</c:choose>
	</div>
	<div style="clear:both"></div>
</div>
