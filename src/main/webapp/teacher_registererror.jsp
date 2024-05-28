<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
</head>
<body>
    <h2>Error</h2>
    <%
        jp.main.model.Teacher teacher = (jp.main.model.Teacher) request.getAttribute("teacher");
        String errorType = request.getParameter("error");
    %>
    <p>教師番号: <%= teacher != null ? teacher.getId() : "不明" %></p>
    <p>教師名: <%= teacher != null ? teacher.getName() : "不明" %></p>
    <% if ("sql".equals(errorType)) { %>
        <p>教師番号のチェック中にエラーが発生しました。</p>
    <% } else if ("general".equals(errorType)) { %>
        <p>予期しないエラーが発生しました。</p>
    <% } else if ("duplicate".equals(errorType)) { %>
        <p>教師番号が既に存在します。</p>
    <% } %>
</body>
</html>
