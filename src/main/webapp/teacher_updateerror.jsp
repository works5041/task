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
        .info {
            display: inline-block;
            text-align: left;
            margin-top: 20px;
        }
        .teacher-info {
            margin-top: 20px;
        }
        .back-link {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 20px;
            color: #fff;
            background-color: #007BFF;
            text-decoration: none;
            border: 2px solid #007BFF;
            border-radius: 5px;
        }
        .back-link:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>教師情報の更新に失敗しました。</h1>
    <div class="info">
        <div class="teacher-info">
            <p>教師番号: <%= request.getParameter("id") %></p>
            <p>名前: <%= request.getParameter("name") %></p>
            <p>年齢: <%= request.getParameter("age") %></p>
            <p>性別: <%= request.getParameter("sex") %></p>
            <p>コース: <%= request.getParameter("course") %></p>
        </div>
    </div>
    <br>
    <br>
    <a href="index.jsp" class="back-link">トップページに戻る</a>
</body>
</html>
