<%--
  Created by IntelliJ IDEA.
  User: l
  Date: 2019-11-27
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:h="http://xmlns.jpc.org/jsf/html" xmlns:f="http://xmlns.jpc.org/jsf/core">
<head>
    <title>Login page</title>
</head>
<body>
<h1> ${error ? "incorrect login information" : "Login page"} </h1>
    <form action="./login" method="post">
        login:<br>
        <input type="text" name="login" placeholder="login">
        <br>
        password:<br>
        <input type="text" name="pass" placeholder="password">
        <br><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
