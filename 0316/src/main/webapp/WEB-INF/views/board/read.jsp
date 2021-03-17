<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

<script>

const bno = 509
const service = (function(){
	function getList(){
        return fetch("http://localhost:8080/replies/list",
            {
                method:"get"
            }).then(res => res.json())
            }
	
    }
	service().getList().then(result =>{
		console.log(result)
	})
	
})

</script>

</body>
</html>