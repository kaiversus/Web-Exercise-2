<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<footer class="bg-dark text-light mt-auto py-4">
	<div class="container">
		<div class="row gy-3">

			<div class="col-md-6">
				<h6 class="fw-bold mb-0">
					<i class="bi bi-shop me-1"></i>52HZ-platform
				</h6>
			</div>

			<div class="col-md-3">
				<h6 class="fw-bold mb-2">Liên kết</h6>
				<ul class="list-unstyled small mb-0">
					<li><a class="link-light link-opacity-75" href="<c:url value='/home'/>">Trang chủ</a></li>
					<li><a class="link-light link-opacity-75" href="<c:url value='/product'/>">Sản phẩm</a></li>
				</ul>
			</div>

			<div class="col-md-3">
				<h6 class="fw-bold mb-2">Tài khoản</h6>
				<ul class="list-unstyled small mb-0">
					<c:choose>
						<c:when test="${not empty sessionScope.account}">
							<li><a class="link-light link-opacity-75" href="<c:url value='/profile'/>">Trang cá nhân</a></li>
							<li><a class="link-light link-opacity-75" href="<c:url value='/logout'/>">Đăng xuất</a></li>
						</c:when>
						<c:otherwise>
							<li><a class="link-light link-opacity-75" href="<c:url value='/login'/>">Đăng nhập</a></li>
							<li><a class="link-light link-opacity-75" href="<c:url value='/register'/>">Đăng ký</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

		</div>

		<hr class="border-secondary my-3">
		<p class="mb-0 text-center text-white-50 small">
			&copy; 2026 Đinh Thiên Bảo - 24162009
		</p>
	</div>
</footer>
