<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <h1>エラーが発生しました。</h1>
    <div class="info">
        <div class="teacher-info">
            <%
                jp.main.model.Teacher teacher = (jp.main.model.Teacher) request.getAttribute("teacher");
                String errorType = request.getParameter("error");
            %>
            <p>教師番号: <%= teacher != null ? teacher.getId() : "不明" %></p>
            <p>名前: <%= teacher != null ? teacher.getName() : "不明" %></p>
            <p>年齢: <%= teacher != null ? teacher.getAge() : "不明" %></p>
            <p>性別: <%= teacher != null ? teacher.getSex() : "不明" %></p>
            <p>コース: <%= teacher != null ? teacher.getCourse() : "不明" %></p>
            <%
            if ("sql".equals(errorType)) {
                out.println("<p>教師番号のチェック中にエラーが発生しました。</p>");
            } else if ("general".equals(errorType)) {
                out.println("<p>予期しないエラーが発生しました。</p>");
            } else if ("duplicate".equals(errorType)) {
                out.println("<p>教師番号が既に存在します。</p>");
            }
            %>
        </div>
    </div>
    <br>
    <br>
    <a href="index.jsp" class="back-link">トップページに戻る</a>
</body>
</html>
