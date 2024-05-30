<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>成功</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 50px;
        }
        h1 {
            color: green;
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
    <h1>教師情報の登録に成功しました。</h1>
    <%-- リクエスト属性から教師情報を取得して表示 --%>
    <%
        jp.main.model.Teacher registeredTeacher = (jp.main.model.Teacher)request.getAttribute("registeredTeacher");
        if (registeredTeacher != null) { // null チェックを追加
    %>
        <p>教師番号: <%= registeredTeacher.getId() %></p>
        <p>名前: <%= registeredTeacher.getName() %></p>
        <p>年齢: <%= registeredTeacher.getAge() %></p>
        <p>性別: <%= registeredTeacher.getSex() %></p>
        <p>コース: <%= registeredTeacher.getCourse() %></p>
    <% } else { %>
    <p>教師情報が取得できませんでした。</p>
    <% } %>
    <a href="index.jsp">トップページに戻る</a>
</body>
</html>
