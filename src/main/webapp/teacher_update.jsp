<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jp.main.model.Teacher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>教師情報更新</title>
    <style>
        /* コンテナのスタイル */
        .container {
            width: 60%; /* コンテナの幅を適切なサイズに設定 */
            margin: 0 auto; /* 左右のマージンを自動で設定して中央に揃える */
            text-align: center; /* テキストを中央揃えにする */
        }
        /* 各要素のスタイル */
        h1, h2, p, form {
            margin: 20px 0; /* 各要素の上下のマージンを設定 */
        }
    </style>
</head>
<body>
    <div class="container"> <!-- コンテナを作成 -->
    <h1>教師情報更新</h1>
    <form action="teacherUpdateConfirm.jsp" method="post" accept-charset="UTF-8"> <!-- 追加 -->
        <h2>教師番号</h2>
        <input type="number" name="id" value="<%= request.getParameter("id") %>"><br>
        <h2>名前</h2>
        <input type="text" name="name" />
        <h2>年齢</h2>
        <input type="number" name="age" />
        <h2>性別</h2>
        <input type="radio" name="sex" value="男性">男性
        <input type="radio" name="sex" value="女性">女性
        <h2>コース</h2>
        <select name="course" id="course">
            <option value="">コースを選択</option>
            <option value="日本語">日本語</option>
            <option value="数学">数学</option>
            <option value="英語">英語</option>
            <option value="世界史">世界史</option>
            <option value="物理">物理</option>
        </select>
        <br><br>
        <input type="submit" value="更新" /> <input type="reset" value="リセット" />
                </form>
            </div>
        </body>
        </html>