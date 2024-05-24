<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>教師情報登録</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 20px;
        }
        form {
            display: inline-block;
            text-align: left;
        }
        h2 {
            color: #333;
        }
        input[type="text"], input[type="number"], select {
            width: 100%;
            padding: 5px;
            margin: 10px 0;
        }
        input[type="radio"] {
            margin: 0 10px;
        }
        input[type="submit"], input[type="reset"] {
            padding: 10px 20px;
            margin: 10px 5px;
            color: #fff;
            background-color: #007BFF;
            border: none;
            border-radius: 5px;
        }
        input[type="submit"]:hover, input[type="reset"]:hover {
            background-color: #0056b3;
        }
        .error {
            color: red;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            function checkTeacherId(tid) {
                return $.ajax({
                    url: "TeacherServlet?action=checkId",
                    type: "GET",
                    data: { tid: tid },
                    dataType: "json"
                });
            }

            $("form").on("submit", function(event) {
                event.preventDefault();
                var form = this;
                var valid = true;
                $("input[required]").each(function() {
                    if ($(this).val() === "") {
                        valid = false;
                        $(this).next(".error").remove();
                        $(this).after("<span class='error'>このフィールドは必須です。</span>");
                    } else {
                        $(this).next(".error").remove();
                    }
                });

                if (valid) {
                    var tid = $("input[name='tid']").val();
                    checkTeacherId(tid).done(function(data) {
                        if (data.exists) {
                            alert("この教師番号は既に存在します。");
                        } else {
                            form.submit();
                        }
                    });
                }
            });

            $("input[name='tid']").on("blur", function() {
                var tid = $(this).val();
                checkTeacherId(tid).done(function(data) {
                    if (data.exists) {
                        alert("この教師番号は既に存在します。");
                    }
                });
            });
        });
    </script>
</head>
<body>
    <h1>教師情報登録</h1>
    <form action="insert" method="post" accept-charset="UTF-8">
        <h2>教師番号</h2>
        <input type="number" name="tid" required><br>
        <h2>名前</h2>
        <input type="text" name="name" required><br>
        <h2>年齢</h2>
        <input type="number" name="age" required><br>
        <h2>性別</h2>
        <input type="radio" name="sex" value="男性" required>男性
        <input type="radio" name="sex" value="女性" required>女性<br>
        <h2>コース</h2>
        <select name="course" id="course" required>
            <option value="">コースを選択</option>
            <option value="日本語">日本語</option>
            <option value="数学">数学</option>
            <option value="英語">英語</option>
            <option value="世界史">世界史</option>
            <option value="物理">物理</option>
        </select><br><br>
        <input type="submit" value="登録">
        <input type="reset" value="リセット">
    </form>
</body>
</html>
