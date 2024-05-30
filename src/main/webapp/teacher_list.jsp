<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>教師情報</title>
    <style type="text/css">
        /* CSS 省略 */
    </style>
    <script>
    function validateForm() {
        var teacherId = document.getElementsByName('id')[0].value;
        if (teacherId.length > 5 || isNaN(teacherId)) {
            alert("教師番号は最大５桁の整数で入力してください。");
            return false;
        }
        return true;
    }
    </script>
</head>
<body>
<a href="index.jsp">トップページに戻る</a>
<h1>教師情報</h1>
<style type="text/css">
    body {
        font-family: Arial, sans-serif;
        text-align: center;
        margin-top: 20px;
    }
    table {
        margin: 0 auto;
        border-collapse: collapse;
        table-layout: fixed; /* テーブルの幅を均等に分配 */
        width: 70%; /* テーブルの幅を設定 */
    }
    th, td {
        border: 1px solid black;
        padding: 10px;
        width: 20%; /* 各列の幅を均等に割り当てる */
        line-height: 1; /* 行の高さを設定 */
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

<form action="search" method="get" accept-charset="UTF-8" onsubmit="return validateForm()">
    教師番号: <input type="number" name="id">
    名前: <input type="text" name="name">
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

<table border="1">
    <tr>
        <th>教師番号</th>
        <th>名前</th>
        <th>年齢</th>
        <th>性別</th>
        <th>コース</th>
        <th>操作</th>
    </tr>
    <%
        request.setCharacterEncoding("UTF-8");
        List<Teacher> listTeacher = (List<Teacher>) request.getAttribute("listTeacher");
        String message = (String) request.getAttribute("message");
        if (listTeacher != null && !listTeacher.isEmpty()) {
            for (Teacher teacher : listTeacher) {
    %>
    <tr>
        <td><%= teacher.getId() %></td>
        <td><%= teacher.getName() %></td>
        <td><%= teacher.getAge() %></td>
        <td><%= teacher.getSex() %></td>
        <td><%= teacher.getCourse() %></td>
        <td>
            <a href="teacher_update.jsp?tid=<%= teacher.getId() %>">更新</a>
        </td>
    </tr>
    <%
            }
        } else if (message != null) {
    %>
    <tr>
        <td colspan="6"><%= message %></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
