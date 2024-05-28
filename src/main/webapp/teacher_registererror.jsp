<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
</head>
<body>
    <h2>Error</h2>
    <% String errorType = request.getParameter("error");
    if ("sql".equals(errorType)) { %>
        <p>教師番号のチェック中にエラーが発生しました。</p>
    <% } else if ("general".equals(errorType)) { %>
        <p>予期しないエラーが発生しました。</p>
    <% } %>
</body>
</html>
