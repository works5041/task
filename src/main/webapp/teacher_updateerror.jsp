<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja"> <!-- 言語属性を追加 -->
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
                    <p>教師番号: <%= request.getParameter("id") %></p>
                    <p>名前: <%= request.getParameter("name") %></p>
                    <p>年齢: <%= request.getParameter("age") %></p>
                    <p>性別: <%= request.getParameter("sex") %></p>
                    <p>コース: <%= request.getParameter("course") %></p>
    <a href="index.jsp">トップページに戻る</a>
</body>
</html>
