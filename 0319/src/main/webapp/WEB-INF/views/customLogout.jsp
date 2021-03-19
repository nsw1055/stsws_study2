<%--
  Created by IntelliJ IDEA.
  User: nsw10
  Date: 2021-03-19
  Time: 오후 3:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/customLogout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <button>LOGOUT</button>
</form>

</body>
</html>
