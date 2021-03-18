<%--
  Created by IntelliJ IDEA.
  User: nsw10
  Date: 2021-03-18
  Time: 오후 12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<button id="saveBtn">register</button>

<script>

    document.querySelector("#saveBtn").addEventListener("click", function (e) {

        const arr = [
            {fileName : "aaa.jpg", uuid:"11111", uploadPath:"2021/03/18"},
            {fileName : "bbb.jpg", uuid:"222222", uploadPath:"2021/03/18"}
        ]

        const obj = {
            title: "title",
            content:"Content",
            writer:"user00",
            fileList: arr}

        fetch("/board/register",
            {
                method: 'post',
                headers: {'Content-type': 'application/json; charset=UTF-8'},
                body: JSON.stringify(obj)
            })

    }, false);

</script>


</body>
</html>
