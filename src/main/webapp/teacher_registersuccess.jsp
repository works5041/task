<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
    <h1>操作が成功しました。</h1>
    <%-- リクエスト属性から教師情報を取得して表示 --%>
    <%
        jp.main.model.Teacher registeredTeacher = (jp.main.model.Teacher)request.getAttribute("registeredTeacher");
    %>
    <p>登録された教師番号: <%= registeredTeacher.getId() %></p>
    <p>登録された教師名: <%= registeredTeacher.getName() %></p>
    <a href="index.jsp">トップページに戻る</a>
</body>
</html>
