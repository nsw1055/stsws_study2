<%--
  Created by IntelliJ IDEA.
  User: nsw10
  Date: 2021-03-19
  Time: 오후 2:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/login" method="post">
    <input type="text" name="username">
    <input type="password" name="password">
    <sec:csrfInput/>
    <button>LOGIN</button>
</form>

</body>
</html>