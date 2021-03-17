
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


