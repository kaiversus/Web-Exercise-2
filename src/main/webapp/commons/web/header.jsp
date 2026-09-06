<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
	<div class="container">

		<a class="navbar-brand fw-bold" href="<c:url value='/home'/>">
			<i class="bi bi-shop me-1"></i>52HZ-platform
		</a>

		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false"
			aria-label="Mở menu">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="mainNav">

			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item">
					<a class="nav-link" href="<c:url value='/home'/>">Trang chủ</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="<c:url value='/product'/>">Sản phẩm</a>
				</li>
			</ul>

			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
				<c:choose>
					<c:when test="${not empty sessionScope.account}">

						<c:if test="${sessionScope.account.role == 1}">
							<li class="nav-item dropdown">
								<a class="nav-link dropdown-toggle" href="#" role="button"
									data-bs-toggle="dropdown" aria-expanded="false">
									<i class="bi bi-gear me-1"></i>Quản trị
								</a>
								<ul class="dropdown-menu dropdown-menu-end">
									<li><a class="dropdown-item" href="<c:url value='/admin/products'/>">Sản phẩm</a></li>
									<li><a class="dropdown-item" href="<c:url value='/admin/categories'/>">Danh mục</a></li>
								</ul>
							</li>
						</c:if>

						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" role="button"
								data-bs-toggle="dropdown" aria-expanded="false">
								<i class="bi bi-person-circle me-1"></i>
								<c:out value="${sessionScope.account.username}"/>
							</a>
							<ul class="dropdown-menu dropdown-menu-end">
								<li><a class="dropdown-item" href="<c:url value='/profile'/>">Trang cá nhân</a></li>
								<li><hr class="dropdown-divider"></li>
								<li>
									<a class="dropdown-item text-danger" href="<c:url value='/logout'/>">
										<i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
									</a>
								</li>
							</ul>
						</li>

					</c:when>
					<c:otherwise>
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/login'/>">Đăng nhập</a>
						</li>
						<li class="nav-item">
							<a class="btn btn-light btn-sm ms-lg-2 mt-2 mt-lg-0"
								href="<c:url value='/register'/>">Đăng ký</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>

		</div>
	</div>
</nav>
