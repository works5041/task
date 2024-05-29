<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>教師情報登録</title>
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
            text-align: center; /* テキストを中央揃えにする */
        }

        input[type="number"],
        input[type="text"],
        select {
            margin: 0 auto; /* テキスト入力フィールドとセレクトボックスを中央揃えにする */
            display: block; /* テキスト入力フィールドとセレクトボックスをブロック要素にする */
        }

        input[type="radio"] {
            margin: 0 auto; /* ラジオボタンを中央揃えにする */
            display: inline-block; /* ラジオボタンをインラインブロック要素にする */
        }

        /* リンクのスタイル */
                .link-box {
                    background-color: blue; /* 背景色を青にする */
                    display: inline-block; /* リンクをインラインブロック要素にする */
                    padding: 5px 10px; /* 内側の余白を設定 */
                    border-radius: 5px; /* 角丸にする */
                }

                .link-box a {
                    color: white; /* リンクの文字色を白にする */
                    text-decoration: none; /* リンクの下線を消す */
                }
    </style>
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
            if (tid === "") {
                alert("教師番号を入力してください");
                document.forms["teacherForm"]["tid"].focus();
                return false;
            }
            if (name === "") {
                alert("名前を入力してください");
                document.forms["teacherForm"]["name"].focus();
                return false;
            }
            if (age === "") {
                alert("年齢を入力してください");
                document.forms["teacherForm"]["age"].focus();
                return false;
            }
            if (sex === "") {
                alert("性別を選択してください");
                return false;
            }
            if (course === "") {
                alert("コースを選択してください");
                document.forms["teacherForm"]["course"].focus();
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
<div class="container">
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
        <FONT COLOR="red"> *全ての項目を入力してください </FONT>
        <br>
        <br>
        <input type="submit" value="登録" /> <input type="reset" value="リセット" />
    </form>
    <br>
    <div class="link-box"><a href="index.jsp">トップページに戻る</a></div>
</div>
</body>
</html>
