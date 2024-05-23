<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html lang="ja"> <!-- 言語属性を追加 -->
<head>
    <meta charset="UTF-8">
    <title>教師情報</title>
    <style type="text/css">
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 20px;
        }
        table {
            margin: 0 auto;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 10px;
        }
        th {
            background-color: #f2f2f2;
        }
        form {
            margin: 20px auto;
            text-align: center;
        }
        input[type="text"], input[type="number"], select {
            padding: 5px;
            margin-right: 10px;
        }
        input[type="submit"] {
            padding: 5px 10px;
            color: #fff;
            background-color: #007BFF;
            border: none;
            border-radius: 3px;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
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

<h1>教師情報</h1>

<form action="/search" method="get" accept-charset="UTF-8">
    教師番号: <input type="number" name="id">
    教師名: <input type="text" name="name">
    コース:
    <select name="course">
        <option value="">コースを選択</option>
        <option value="日本語">日本語</option>
        <option value="数学">数学</option>
        <option value="英語">英語</option>
        <option value="世界史">世界史</option>
        <option value="物理">物理</option>
    </select>
    <input type="submit" value="検索">
</form>

<a href="/new">新規登録</a>

<table border="1">
    <tr>
        <th>ID</th>
        <th>名前</th>
        <th>年齢</th>
        <th>性別</th>
        <th>コース</th>
        <th>アクション</th>
    </tr>
    <%
        List<Teacher> listTeacher = (List<Teacher>) request.getAttribute("listTeacher");
        if (listTeacher != null) {
            for (Teacher teacher : listTeacher) {
    %>
    <tr>
        <td><%= teacher.getId() %></td>
        <td><%= teacher.getName() %></td>
        <td><%= teacher.getAge() %></td>
        <td><%= teacher.getSex() %></td>
        <td><%= teacher.getCourse() %></td>
        <td>
            <a href="edit?id=<%= teacher.getId() %>">編集</a>
            <a href="delete?id=<%= teacher.getId() %>">削除</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
