<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>更新内容の確認</title>
</head>
<body>
    <h1>更新内容の確認</h1>
    <p>以下の情報で更新を実行しますか？</p>
    <ul>
        <li>教師番号: <%= new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8") %></li>
        <li>名前: <%= new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8") %></li>
        <li>年齢: <%= request.getParameter("age") %></li>
        <li>性別: <%= new String(request.getParameter("sex").getBytes("ISO-8859-1"), "UTF-8") %></li>
        <li>コース: <%= new String(request.getParameter("course").getBytes("ISO-8859-1"), "UTF-8") %></li>
    </ul>
    <form action="update" method="post" accept-charset="UTF-8"> <!-- 追加 -->
        <input type="hidden" name="id" value="<%= new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8") %>">
        <input type="hidden" name="name" value="<%= new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8") %>">
        <input type="hidden" name="age" value="<%= request.getParameter("age") %>">
        <input type="hidden" name="sex" value="<%= new String(request.getParameter("sex").getBytes("ISO-8859-1"), "UTF-8") %>">
        <input type="hidden" name="course" value="<%= new String(request.getParameter("course").getBytes("ISO-8859-1"), "UTF-8") %>">
        <input type="submit" value="はい、更新します">
    </form>
    <a href="javascript:history.back()">いいえ、キャンセルします</a>
</body>
</html>
