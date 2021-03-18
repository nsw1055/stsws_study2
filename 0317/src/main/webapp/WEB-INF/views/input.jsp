<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div class = "inputDiv">
<input type='file' name='uploadFile' multiple="multiple">
<button id ="uploadBtn">Upload</button>
</div>
<ul class="uploadResult">
</ul>


<script>
	const uploadUL = document.querySelector(".uploadResult")

    const input = document.querySelector("input[name='uploadFile']");

    const cloneInput = document.querySelector("input[name='uploadFile']").outerHTML

    console.dir(input.outerHTML)



    document.querySelector("#uploadBtn").addEventListener("click", function(){

        const formData = new FormData();

        const files = input.files;

        console.dir(input);

        for(let i = 0; i < files.length; i++){
            formData.append("files", files[i]);
        }
        
        fetch("/upload",{
            method: "post",
            body: formData
        }).then(res => res.json())
            .then(jsonObj => {
        	console.log(jsonObj)
        	
        	let htmlCode = "";
        	for (let i = 0; i < jsonObj.length; i++) {
				fileObj = jsonObj[i];
				console.log(fileObj.thumbLink)
				htmlCode += "<li><img src='/view?file="+fileObj.thumbLink+"'></li>"


			}
        	uploadUL.innerHTML+= htmlCode;

                document.querySelector("input[name='uploadFile']").remove()

                document.querySelector(".inputDiv").insertAdjacentHTML('afterbegin', cloneInput)
        })



    }, false)

</script>
</body>