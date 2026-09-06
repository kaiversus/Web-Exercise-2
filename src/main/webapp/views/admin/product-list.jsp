<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý sản phẩm</title>
</head>
<body>

<div class="d-flex flex-wrap align-items-center justify-content-between mb-3">
	<h3 class="mb-0"><i class="bi bi-box-seam me-2"></i>Quản lý sản phẩm</h3>
	<div>
		<a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary btn-sm">
			<i class="bi bi-tags me-1"></i>Danh mục
		</a>
		<a href="<c:url value='/admin/product/add'/>" class="btn btn-primary btn-sm">
			<i class="bi bi-plus-lg me-1"></i>Thêm sản phẩm
		</a>
	</div>
</div>

<div class="card border-0 shadow-sm">
	<div class="table-responsive">
		<table class="table table-hover align-middle mb-0">
			<thead class="table-light">
				<tr>
					<th style="width:60px">#</th>
					<th style="width:110px">Ảnh</th>
					<th>Tên sản phẩm</th>
					<th>Danh mục</th>
					<th class="text-end">Giá</th>
					<th class="text-center">SL</th>
					<th class="text-center">Trạng thái</th>
					<th class="text-center" style="width:150px">Thao tác</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listproduct}" var="p" varStatus="stt">
					<c:set var="img" value="${empty p.images ? 'avatar.png' : p.images}"/>
					<c:choose>
						<c:when test="${fn:startsWith(img, 'http')}">
							<c:set var="imgUrl" value="${img}"/>
						</c:when>
						<c:otherwise>
							<c:url value="/image" var="imgUrl">
								<c:param name="fname" value="${img}"/>
							</c:url>
						</c:otherwise>
					</c:choose>

					<tr>
						<td>${stt.index + 1}</td>
						<td>
							<img src="${imgUrl}" class="rounded border"
								style="width:90px;height:64px;object-fit:cover" alt="">
						</td>
						<td class="fw-semibold"><c:out value="${p.productname}"/></td>
						<td><c:out value="${p.category.categoryname}"/></td>
						<td class="text-end text-danger fw-semibold">
							<fmt:formatNumber value="${p.price}" type="number"/> đ
						</td>
						<td class="text-center">${p.quantity}</td>
						<td class="text-center">
							<c:choose>
								<c:when test="${p.status == 1}">
									<span class="badge text-bg-success">Hiển thị</span>
								</c:when>
								<c:otherwise>
									<span class="badge text-bg-secondary">Ẩn</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="text-center">
							<a href="<c:url value='/admin/product/edit'/>?id=${p.productid}"
								class="btn btn-sm btn-outline-primary">
								<i class="bi bi-pencil"></i>
							</a>
							<form action="<c:url value='/admin/product/delete'/>" method="post"
								class="d-inline"
								onsubmit="return confirm('Bạn chắc chắn muốn xoá sản phẩm này?')">
								<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
								<input type="hidden" name="id" value="${p.productid}">
								<button type="submit" class="btn btn-sm btn-outline-danger">
									<i class="bi bi-trash"></i>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>

				<c:if test="${empty listproduct}">
					<tr>
						<td colspan="8" class="text-center text-secondary py-4">
							Chưa có sản phẩm nào.
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>

</body>
</html>
