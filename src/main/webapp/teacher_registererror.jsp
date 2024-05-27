<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>エラー</title>
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
    <h1>エラーが発生しました。</h1>
    <%-- リクエスト属性から教師情報を取得して表示 --%>
        <p>登録された教師番号: <%= request.getAttribute("teacherId") %></p>
        <p>登録された教師名: <%= request.getAttribute("teacherName") %></p>
    <a href="index.jsp">トップページに戻る</a>
</body>
</html>
