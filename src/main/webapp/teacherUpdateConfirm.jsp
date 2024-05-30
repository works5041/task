<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>更新内容の確認</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 50px;
        }
        h1 {
            color: red;
        }
        a {
            display: inline-block;
            margin: 10px 20px;
            padding: 10px 20px;
            color: #fff;
            background-color: #007BFF;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>更新内容の確認</h1>
    <p>以下の情報で更新を実行しますか？</p>
    <p>教師番号: <%= request.getParameter("tid") %></p>
    <p>名前: <%= request.getParameter("name") %></p>
    <p>年齢: <%= request.getParameter("age") %></p>
    <p>性別: <%= request.getParameter("sex") %></p>
    <p>コース: <%= request.getParameter("course") %></p>

    <form action="TeacherServlet?action=update" method="post" accept-charset="UTF-8">
        <input type="hidden" name="tid" value="<%= request.getParameter("tid") %>">
        <input type="hidden" name="name" value="<%= request.getParameter("name") %>">
        <input type="hidden" name="age" value="<%= request.getParameter("age") %>">
        <input type="hidden" name="sex" value="<%= request.getParameter("sex") %>">
        <input type="hidden" name="course" value="<%= request.getParameter("course") %>">
        <input type="submit" value="はい、更新します">
    </form>
    <a href="javascript:history.back()">いいえ、キャンセルします</a>
</body>
</html>
