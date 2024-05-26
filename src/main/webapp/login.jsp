
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Login</title>

    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">

    <script>
        window.addEventListener("load", () => {
            document.getElementById("username").addEventListener("keydown", (event) => {
                if (event.key === "Enter") {
                    event.preventDefault()
                    document.getElementById("password").focus()
                    document.getElementById("password").select()
                }
            })
        })
    </script>

</head>
<body>
<div class="formHolder">
    <form action="${pageContext.request.contextPath}/api/login" method="post">
        <div class="formElement">
            <h2>Competition Login</h2>
        </div>

        <div class="formElement">
            <input type="text" placeholder="Username" id="username" name="username" >
        </div>

        <div class="formElement">
            <input type="password" placeholder="Password" id="password" name="password">
        </div>

        <div class="formElement">
            <input type="submit" value="Submit" id="submit">
        </div>
    </form>
</div>
</body>
</html>