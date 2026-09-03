<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title"/></title>
    <sitemesh:write property="head"/>
</head>
<body style="margin: 0; font-family: Arial, sans-serif;">
    <%@ include file="/commons/web/header.jsp"%>
    
    <div style="min-height: 500px; padding: 20px;">
        <sitemesh:write property="body"/>
    </div>
    
    <%@ include file="/commons/web/footer.jsp"%>
</body>
</html>