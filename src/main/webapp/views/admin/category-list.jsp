<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý danh mục</title>
</head>
<body>

<div class="d-flex flex-wrap align-items-center justify-content-between mb-3">
	<h3 class="mb-0"><i class="bi bi-tags me-2"></i>Quản lý danh mục</h3>
	<div>
		<a href="<c:url value='/admin/products'/>" class="btn btn-outline-secondary btn-sm">
			<i class="bi bi-box-seam me-1"></i>Sản phẩm
		</a>
		<a href="<c:url value='/admin/category/add'/>" class="btn btn-primary btn-sm">
			<i class="bi bi-plus-lg me-1"></i>Thêm danh mục
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
					<th>Tên danh mục</th>
					<th class="text-center">Trạng thái</th>
					<th class="text-center" style="width:150px">Thao tác</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listcate}" var="cate" varStatus="stt">
					<c:set var="img" value="${empty cate.images ? 'avatar.png' : cate.images}"/>
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
						<td class="fw-semibold"><c:out value="${cate.categoryname}"/></td>
						<td class="text-center">
							<c:choose>
								<c:when test="${cate.status == 1}">
									<span class="badge text-bg-success">Hoạt động</span>
								</c:when>
								<c:otherwise>
									<span class="badge text-bg-secondary">Khóa</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="text-center">
							<a href="<c:url value='/admin/category/edit'/>?id=${cate.categoryid}"
								class="btn btn-sm btn-outline-primary">
								<i class="bi bi-pencil"></i>
							</a>
							<form action="<c:url value='/admin/category/delete'/>" method="post"
								class="d-inline"
								onsubmit="return confirm('Bạn chắc chắn muốn xoá danh mục này?')">
								<input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
								<input type="hidden" name="id" value="${cate.categoryid}">
								<button type="submit" class="btn btn-sm btn-outline-danger">
									<i class="bi bi-trash"></i>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>

				<c:if test="${empty listcate}">
					<tr>
						<td colspan="5" class="text-center text-secondary py-4">
							Chưa có danh mục nào.
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>

</body>
</html>
