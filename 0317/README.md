
파일업로드

1. form 태그
2. Ajax 태그 (보통 많이 사용)

에디터 사용  
예전에는 드래그앤 드롭을 많이 사용하였다 하지만 이 방법은 모바일에서 치명적인 약점이 있으므로 추천하지 않는다.  
전통적인 방법을 사용 할 것이다.
```
<input type = 'file'>
```
위의 코드는 readOnly이다 왜냐하면 파일의 이름을 바꾸면 다른 파일이 들어갈 수 있기 때문이다.  
이때 javaScript의 clone을 사용한다.  
업로드가 되면 몇가지 문제가 생긴다.  
1. 파일이름의 중복
2. 너무 많은 리소스를 쓰게된다. : 썸네일을 사용 -> AWS S3를 사용
3. DB 1:N의 관계가 성립된다.

---
* 개발 순서
1. 설정
파일업로드에는 두가지 설정이 있다.
1) commons-fileupload
2) servlet3

2. 화면 : fetch API

3. 파일저장 -> 결과 봔환 (JSON)
4. 결과출력
5. 썸네일\
6. 파일 삭제
7. 게시물 등록 -> DB
8. 수정

---
* 코드
1. web.xml 상단 변경
```
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	id="WebApp_ID" version="3.1">
```
2. web.xml servlet사이에 코드 추가
```
	<servlet-mapping>
		<servlet-name>appServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
```
3. 기본 화면 제작
```
	<input type='file' name='uploadFile' multiple="multiple">
	<button id ="uploadBtn">Upload</button>
```

4.servlet 버전 3.1.0
5.UploadController.java
```
@RestController 
@Log4j
public class UploadController {
	
	@PostMapping("/upload")
	public ResponseEntity<String> upload(MultipartFile[] files){
		
		log.info("--------------------------");
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
}
```
6. servlet-context.xml 코드 추가
```
	<beans:bean id="multipartResolver"
		class="org.springframework.web.multipart.support.StandardServletMultipartResolver">

	</beans:bean>
```
7. javascript
```
<script>
	document.querySelector("#uploadFile").addEventListener("click", function(){
		
		const formData = new FormData();
		
		const input = document.querySelector("input[name='uploadFile']");
		
		const files = input.files;
		
		console.dir(input);
		
		for(let i = 0; i < files.length; i++){
			formData.append("files", files[i]);
		}
		
		fetch("/upload",{
			method: "post",
			body: formData			
		})
		
	}, false)
	
	</script>
```
8. 실행결과
![image](https://user-images.githubusercontent.com/72544949/111404792-baea2400-8712-11eb-91ec-289764902ca3.png)

9. UploadController.java
```
@RestController
@Log4j
public class UploadController {

	@PostMapping("/upload")
	public ResponseEntity<String> upload(MultipartFile[] files) {

		String uploadFolder = "C:\\upload";

		log.info("--------------------------");

		File uploadPath = new File(uploadFolder, getFolder());
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
			}
		
		// null이나 length 체크
		for (int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];

			String fileName = mfile.getOriginalFilename();

			File saveFile = new File(uploadPath, fileName);

			try {
				mfile.transferTo(saveFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}
```
10. uuid
```
@RestController
@Log4j
public class UploadController {

	@PostMapping("/upload")
	public ResponseEntity<String> upload(MultipartFile[] files) {

		String uploadFolder = "C:\\upload";

		log.info("--------------------------");

		File uploadPath = new File(uploadFolder, getFolder());
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
			}
		
		// null이나 length 체크
		for (int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];

			String fileName = mfile.getOriginalFilename();

			UUID uuid = UUID.randomUUID();
			
			File saveFile = new File(uploadPath, uuid.toString()+"_"+fileName);

			try {
				mfile.transferTo(saveFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}

}
```

11. thumbnail
```

@RestController
@Log4j
public class UploadController {

	@PostMapping("/upload")
	public ResponseEntity<String> upload(MultipartFile[] files) {

		String uploadFolder = "C:\\upload";

		log.info("--------------------------");

		File uploadPath = new File(uploadFolder, getFolder());
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
			}
		
		// null이나 length 체크
		for (int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];

			String fileName = mfile.getOriginalFilename();

			UUID uuid = UUID.randomUUID();
			
			boolean isImage = mfile.getContentType().startsWith("image");
			
			File saveFile = new File(uploadPath, uuid.toString()+"_"+fileName);

			try {
				mfile.transferTo(saveFile);
				
				if(isImage) {
					FileOutputStream fos = new FileOutputStream(new File (uploadPath,"s_" +uuid.toString()+"_"+fileName));  
					
					Thumbnailator.createThumbnail(mfile.getInputStream(), fos, 100, 100);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}

}
```

12. 데이터 반환
```
package org.clazh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileDTO {

	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
}
```

13. uploadController
```

@RestController
@Log4j
public class UploadController {

	@PostMapping("/upload")
	public ResponseEntity<List<AttachFileDTO>> upload(MultipartFile[] files) {

		String uploadFolder = "C:\\upload";

		log.info("--------------------------");
		
		String folderPath = getFolder();

		File uploadPath = new File(uploadFolder, getFolder());
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
			}
		
		// null이나 length 체크
		List<AttachFileDTO> list = new ArrayList<>();
		
		
		for (int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];

			String fileName = mfile.getOriginalFilename();

			UUID uuid = UUID.randomUUID();
			
			boolean isImage = mfile.getContentType().startsWith("image");
			
			File saveFile = new File(uploadPath, uuid.toString()+"_"+fileName);

			try {
				mfile.transferTo(saveFile);
				
				if(isImage) {
					FileOutputStream fos = new FileOutputStream(new File (uploadPath,"s_" +uuid.toString()+"_"+fileName));  
					
					Thumbnailator.createThumbnail(mfile.getInputStream(), fos, 100, 100);
					fos.close();
				}
				
				AttachFileDTO attatchFileDTO = new AttachFileDTO(fileName, folderPath, uuid.toString(), isImage);
				
				
				list.add(attatchFileDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}

}
```
과제로 만든 완성 코드
```
package org.clazh.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.clazh.dto.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@RestController
@Log4j
public class UploadController {
	
	@GetMapping("/view")
	public ResponseEntity<byte[]> viewFile(String file){
		
		byte[] result = null;
		
		ResponseEntity<byte[]> res = null;
		
		try {
			String deStr = URLDecoder.decode(file, "UTF-8");
			
			String originStr = deStr.replace("#",".");
			
			log.info(originStr);
			
			File targetFile = new File("C:\\upload\\"+ originStr);
			
			log.info(targetFile.toPath());
			
			//MIME 
			String mimeType = Files.probeContentType(targetFile.toPath());
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type",mimeType);
			
			res = new ResponseEntity<>(
					FileCopyUtils.copyToByteArray(targetFile),
					header,
					HttpStatus.OK
					);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
	}

	@PostMapping("/upload")
	public ResponseEntity<List<AttachFileDTO>> upload(@RequestParam("files") MultipartFile[] files){

		String uploadFolder = "C:\\upload";
		
		log.info("-------------------------");
		
		String folderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, folderPath);

		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		//null이나 length체크 
		List<AttachFileDTO> list = new ArrayList<>();
		
		for(int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];
			
			String fileName = mfile.getOriginalFilename();
			
			UUID uuid = UUID.randomUUID();
			
			boolean isImage = mfile.getContentType().startsWith("image");
			
			File saveFile = new File(uploadPath, uuid.toString()+"_"+fileName);
			
			try {
				mfile.transferTo(saveFile);
				
				if(isImage) {
					
					FileOutputStream fos = new FileOutputStream(
							new File(uploadPath, "s_"+uuid.toString()+"_"+fileName));
					
					Thumbnailator.createThumbnail(mfile.getInputStream(), 
							fos, 100, 100);
					fos.close();
				}
				
				AttachFileDTO attachFileDTO  = 
						new AttachFileDTO(fileName, folderPath, uuid.toString(), isImage);
				
				list.add(attachFileDTO);
				
			} catch (Exception e) {

				e.printStackTrace();
			} 
			
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}
	
}
```

```
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<input type='file' name='uploadFile' multiple="multiple">
<button id ="uploadBtn">Upload</button>

<ul class="uploadResult">
</ul>


<script>
	const uploadUL = document.querySelector(".uploadResult")

    const inputOri = document.querySelector("input[name='uploadFile']");

    const cloneInput = inputOri.outerHTML

    // console.dir(inputOri.outerHTML)



    document.querySelector("#uploadBtn").addEventListener("click", function(){
        const input = document.querySelector("input[name='uploadFile']");
        const formData = new FormData();

        const files = input.files;

        // console.dir(input);

        for(let i = 0; i < files.length; i++){
            formData.append("files", files[i]);
        }
        
        fetch("/upload",{
            method: "post",
            body: formData
        }).then(res => res.json())
            .then(jsonObj => {
        	// console.log(jsonObj)
        	
        	let htmlCode = "";
        	for (let i = 0; i < jsonObj.length; i++) {
				fileObj = jsonObj[i];
				console.log(fileObj.thumbLink)
				htmlCode += "<li><img src='/view?file="+fileObj.thumbLink+"'></li>"
			}
        	uploadUL.innerHTML+= htmlCode;

            document.querySelector("input[name='uploadFile']").remove();

            document.querySelector("body").insertAdjacentHTML('afterbegin', cloneInput);

            // console.dir(document.querySelector("input[name='uploadFile']"))
        })
    }, false)

</script>
</body>
```

---
2일차

1. 버튼에 remove  
```
htmlCode += "<li><img src='/view?file="+fileObj.thumbLink+"'><button onclick='removeFile()'>DEL</button></li>"
```

```
  function removeFile() {
        alert("REMOVE FILE")
    }
```

2. parm 전달
```
htmlCode += "<li><img src='/view?file="+fileObj.thumbLink+"'><button onclick='removeFile("+JSON.stringify(fileObj)+")'>DEL</button></li>"
```
```
function removeFile(param) {
        console.log(param)
        alert("REMOVE FILE")
    }
```  
객체를 json문자열로 변경 하여 보냈지만 json문자열이 jsonObj로 변환되어 콘솔에 찍히는걸 확인 할 수 있다.  
![image](https://user-images.githubusercontent.com/72544949/111558663-8f2e7300-87d2-11eb-94a6-82f1dc4245b5.png)  
![image](https://user-images.githubusercontent.com/72544949/111559320-a02bb400-87d3-11eb-98da-2e83a4ebc0d6.png)  
문자열을 함수에서는 객체로 받아들이게되어 객체로 전달되었다 만약 JSON.stringify를 넣지 않는다면 object Object가 들어가므로 속성을 알수 있는 길이 없다.  
  

3. li수정
삭제할 때는 li에 uuid값을 넣어주어 uuid를 찾아 삭제 해 주면 된다.  
```
htmlCode += "<li id ='li_"+fileObj.uuid+"'><img src='/view?file="+fileObj.thumbLink+"'><button onclick='removeFile("+JSON.stringify(fileObj)+")'>DEL</button></li>"
```
![image](https://user-images.githubusercontent.com/72544949/111559904-c00fa780-87d4-11eb-98ef-5e1afa5b57b1.png)  

4. js수정
```
    function removeFile(param) {
        console.log(param)
        alert("REMOVE FILE")

        document.querySelector("#li_"+ param.uuid).remove()
    }
```
5. 파일 삭제 전 controller확인
가장 자주 나는 에러는 415에러(타입이 안맞을 경우)
```
 function removeFile(param) {
        console.log(param)

        fetch("/removeFile",
            {
             method: 'delete',
             headers: {'Content-type': 'application/json; charset=UTF-8'},
             body: JSON.stringify(param)
            })
        document.querySelector("#li_"+ param.uuid).remove()
    }
```
```
//415
@DeleteMapping("/removeFile")
public ResponseEntity<String> removeFile(@RequestBody AttachFileDTO dto){
	log.info(dto);
	log.info("remove.............");
		
	return new ResponseEntity<String>("success", HttpStatus.OK);
}
```
6. 파일 삭제
```
//415
	@DeleteMapping("/removeFile")
	public ResponseEntity<String> removeFile(@RequestBody AttachFileDTO dto){

		log.info(dto);

		log.info("remove.............");

		String filePath = "C:\\upload\\" + dto.getUploadPath();
		String fileName = dto.getUuid()+"_"+dto.getFileName();

		if(dto.isImage()) {

			File thumb = new File(filePath+File.separator+"s_"+fileName);
			thumb.delete();

		}

		File target = new File(filePath+File.separator+fileName);
		target.delete();


		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
```
---
복합 업로드 데이터 수집 공부

1. BoardDTO.java
```
@Data
public class BoardDTO {
    private Integer bno;
    private String title, content,writer;
    private List<AttachFileDTO> fileList;
}
```
2. BoardController.java
```
@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {

    @GetMapping("/register")
    public void registerGet() {

    }

    @PostMapping("/register")
    @ResponseBody
    public String registerPost(@RequestBody BoardDTO dto) {

        log.info("post...................");
        log.info(dto);

        return "success";
    }
}
```

3. register.jsp 접속 확인
```
<button id="saveBtn">register</button>

<script>

    document.querySelector("#saveBtn").addEventListener("click", function (e) {

        const obj = {title: "title", content:"Content", writer:"user00"}

        fetch("/board/register",
            {
                method: 'post',
                headers: {'Content-type': 'application/json; charset=UTF-8'},
                body: JSON.stringify(obj)
            })

    }, false);

</script>
```

4. 전송
```
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
```
5. 코드 합체
```
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

    document.querySelector("#saveBtn")
        .addEventListener("click", function(e){



            const obj  = {
                title:"Title",
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
    const arr = []

	const uploadUL = document.querySelector(".uploadResult")

    const inputOri = document.querySelector("input[name='uploadFile']");

    const cloneInput = inputOri.outerHTML

    // console.dir(inputOri.outerHTML)



    document.querySelector("#uploadBtn").addEventListener("click", function(){
        const input = document.querySelector("input[name='uploadFile']");
        const formData = new FormData();

        const files = input.files;

        // console.dir(input);

        for(let i = 0; i < files.length; i++){
            formData.append("files", files[i]);
        }
        
        fetch("/upload",{
            method: "post",
            body: formData
        }).then(res => res.json())
            .then(jsonObj => {
        	// console.log(jsonObj)
        	
        	let htmlCode = "";
        	for (let i = 0; i < jsonObj.length; i++) {
				let fileObj = jsonObj[i];
                arr.push(fileObj)
				console.log(fileObj.thumbLink)
				htmlCode += "<li id ='li_"+fileObj.uuid+"'><img src='/view?file="+fileObj.thumbLink+"'><button onclick='removeFile("+JSON.stringify(fileObj)+")'>DEL</button></li>"
			}
        	uploadUL.innerHTML+= htmlCode;

            document.querySelector("input[name='uploadFile']").remove();

            document.querySelector(".inputDiv").insertAdjacentHTML('afterbegin', cloneInput);

            // console.dir(document.querySelector("input[name='uploadFile']"))
        })
    }, false)

    function removeFile(param) {
        console.log(param)

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
```


---


	
