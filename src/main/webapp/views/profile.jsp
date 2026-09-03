<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kai.entity.User" %>
<%
    User user = (User) request.getAttribute("user");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cập nhật Profile</title>
</head>

<body>
    <h2>Thông tin cá nhân</h2>

    <% if (message != null && !message.isEmpty()) { %>
        <p style="color: green; font-weight: bold;"><%= message %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/profile" method="post" enctype="multipart/form-data">
        <table cellpadding="8">
            <tr>
                <td>Tên đăng nhập:</td>
                <td><input type="text" name="username" value="<%= user != null ? user.getUsername() : "" %>" readonly style="background:#eee;" /></td>
            </tr>
            <tr>
                <td>Họ và tên:</td>
                <td><input type="text" name="fullname" value="<%= user != null ? user.getFullname() : "" %>" /></td>
            </tr>
            <tr>
                <td>Số điện thoại:</td>
                <td><input type="text" name="phone" value="<%= user != null ? user.getPhone() : "" %>" /></td>
            </tr>
            <tr>
                <td>Ảnh đại diện hiện tại:</td>
                <td>
                    <% if (user != null && user.getImages() != null && !user.getImages().isEmpty()) { %>
                        <img src="<%= request.getContextPath() %>/uploads/<%= user.getImages() %>" alt="Avatar" width="120" style="border: 1px solid #ccc;" />
                    <% } else { %>
                        <i>Chưa có ảnh</i>
                    <% } %>
                </td>
            </tr>
            <tr>
                <td>Chọn ảnh mới:</td>
                <td><input type="file" name="images" accept="image/*" /></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <button type="submit" style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; cursor: pointer;">Cập nhật thông tin</button>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>