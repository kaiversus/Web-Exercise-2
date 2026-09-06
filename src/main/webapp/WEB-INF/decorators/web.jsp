<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title"/></title>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	integrity="sha384-XGjxtQfXaH2tnPFa9x+ruJTuLE3Aa6LhHSWRr1XeTyhezb4abCG4ccI5AkVDxqC+"
	crossorigin="anonymous">

<style>
:root { --brand: #0d6efd; }
body { background-color: #f5f7fa; }
.product-card { transition: box-shadow .15s ease, transform .15s ease; }
.product-card:hover { box-shadow: 0 .5rem 1rem rgba(0,0,0,.12); transform: translateY(-2px); }
.product-thumb { height: 180px; object-fit: cover; }
.product-title { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
                 overflow: hidden; min-height: 3rem; }
.desc-block { white-space: pre-wrap; line-height: 1.7; }
footer a { text-decoration: none; }
</style>

<sitemesh:write property="head"/>
</head>

<body class="d-flex flex-column min-vh-100">

	<%@ include file="/commons/web/header.jsp"%>

	<main class="container my-4 flex-grow-1">
		<sitemesh:write property="body"/>
	</main>

	<%@ include file="/commons/web/footer.jsp"%>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>
