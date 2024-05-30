<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.main.model.Teacher" %>
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
    <h1>教師情報の更新が成功しました。</h1>
    <div class="info">
        <div class="teacher-info">
            <%
            try {
                Teacher teacher = (Teacher) request.getAttribute("teacher");
                if (teacher == null) {
                    throw new NullPointerException("Teacher attribute is missing.");
                }
            %>
                <p>教師番号: <%= teacher.getId() %></p>
                <p>名前: <%= teacher.getName() %></p>
                <p>年齢: <%= teacher.getAge() %></p>
                <p>性別: <%= teacher.getSex() %></p>
                <p>コース: <%= teacher.getCourse() %></p>
            <%
            } catch (Exception e) {
                out.println("Error: " + e.getMessage() + "<br>");
            }
            %>
        </div>
    </div>
    <br>
    <br>
    <a href="list" class="back-link">教師情報に戻る</a>
</body>
</html>
