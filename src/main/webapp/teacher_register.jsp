<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>教師情報登録</title>
    <script>
        function validateForm() {
            var tid = document.forms["teacherForm"]["tid"].value;
            var name = document.forms["teacherForm"]["name"].value;
            var age = document.forms["teacherForm"]["age"].value;
            var sex = document.forms["teacherForm"]["sex"].value;
            var course = document.forms["teacherForm"]["course"].value;

            // 教師番号が数値でない場合の警告
            if (isNaN(tid)) {
                alert("教師番号は数値で入力してください");
                return false;
            }

            // 未入力項目がある場合の警告
            if (tid === "" || name === "" || age === "" || sex === "" || course === "") {
                alert("全ての項目を入力してください");
                return false;
            }

            // 重複チェックを行うためにAjaxリクエストを送信
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var response = JSON.parse(xhr.responseText);
                    if (response.exists) {
                        alert("教師番号が既に存在します");
                        return false;
                    } else {
                        // 重複しない場合はフォームの送信を許可
                        document.forms["teacherForm"].submit();
                    }
                }
            };
            xhr.open("GET", "checkId?tid=" + tid, true);
            xhr.send();

            return false; // フォームのデフォルトの送信を防止
        }
    </script>
</head>
<body>
<h1>教師情報登録</h1>
<form name="teacherForm" action="insert" method="post" onsubmit="return validateForm()">
    <h2>教師番号</h2>
    <input type="number" name="tid"><br>
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
    <br>
    <br>
    *印が付いている項目は必須項目です
    <br>
    <input type="submit" value="登録" /> <input type="reset" value="リセット" />
</form>
</body>
</html>
