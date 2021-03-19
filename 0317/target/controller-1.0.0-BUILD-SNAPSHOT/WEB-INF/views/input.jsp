<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<hr/>
<button id="saveBtn">Register</button>
<hr/>
<hr/>

<div class="inputDiv">
<input type='file' name='uploadFile'  multiple="multiple">
<button id="uploadBtn">Upload</button>
</div>
<ul class="uploadResult">
</ul>

<script>
    function test(){
        var arr = []

        return arr;
    }
    document.querySelector("#saveBtn")
        .addEventListener("click", function(e){



            const obj  = {
                title:"Title",
                content:"Content",
                writer:"user00",
                fileList: test()}

            fetch("/board/register",
                {
                    method: 'post',
                    headers: {'Content-type': 'application/json; charset=UTF-8'},
                    body: JSON.stringify(obj)
                })

        }, false);


	const uploadUL = document.querySelector(".uploadResult")

    const inputOri = document.querySelector("input[name='uploadFile']");

    console.dir(inputOri)
    const cloneInput = inputOri.outerHTML

    // console.dir(inputOri.outerHTML)

    document.querySelector("#uploadBtn").addEventListener("click", function(){
        const input = document.querySelector("input[name='uploadFile']");
        const formData = new FormData();

        const files = input.files;

        // console.dir(input);


        //const arr = []

        for(let i = 0; i < files.length; i++){
            formData.append("files", files[i]);
        }
        
        fetch("/upload",{
            method: "post",
            body: formData
        }).then(res => res.json())
            .then(jsonObj => {
        	// console.log(jsonObj)
        	const arr2 = test()
        	let htmlCode = "";
        	for (let i = 0; i < jsonObj.length; i++) {
				let fileObj = jsonObj[i];
                arr2.push(fileObj)
                console.log(arr2)
                console.dir(fileObj)
                console.log("===================================")
                console.dir(JSON.stringify(fileObj))
				console.log(fileObj.thumbLink)
				htmlCode += "<li id ='li_"+fileObj.uuid+"'><img src='/view?file="+fileObj.thumbLink+"'><button onclick='removeFile("+JSON.stringify(fileObj)+")'>DEL</button></li>"
			}
        	uploadUL.innerHTML+= htmlCode;

            document.querySelector("input[name='uploadFile']").remove();

            document.querySelector(".inputDiv").insertAdjacentHTML('afterbegin', cloneInput);

            // console.dir(document.querySelector("input[name='uploadFile']"))
        })
    }, false)

    function removeFile(param){

        console.dir(param)

        removeFile1(param)

    }

    function removeFile1(param) {
        console.dir(param)



        fetch("/removeFile",
            {
             method: 'delete',
             headers: {'Content-type': 'application/json; charset=UTF-8'},
             body: JSON.stringify(param)
            })
        document.querySelector("#li_"+ param.uuid).remove()
    }




</script>
</body>