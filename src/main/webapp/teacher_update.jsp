<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>教師情報更新</title>
    <style>
        .container {
            width: 60%;
            margin: 0 auto;
            text-align: center;
        }
        h1, h2, p, form {
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>教師情報更新</h1>
        <form action="teacherUpdateConfirm.jsp" method="post" accept-charset="UTF-8">
            <h2>教師番号</h2>
            <input type="hidden" name="tid" value="<%= request.getParameter("tid") %>">
            <p><%= request.getParameter("tid") %></p>
            <h2>名前</h2>
            <input type="text" name="name" required />
            <h2>年齢</h2>
            <input type="number" name="age" required />
            <h2>性別</h2>
            <input type="radio" name="sex" value="男性" required>男性
            <input type="radio" name="sex" value="女性" required>女性
            <h2>コース</h2>
            <select name="course" id="course" required>
                <option value="">コースを選択</option>
                <option value="日本語">日本語</option>
                <option value="数学">数学</option>
                <option value="英語">英語</option>
                <option value="世界史">世界史</option>
                <option value="物理">物理</option>
            </select>
            <br><br>
            <input type="submit" value="確認" /> <input type="reset" value="リセット" />
        </form>
    </div>
</body>
</html>
