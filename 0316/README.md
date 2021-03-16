*REST API
웹설계의 우수성에 비해 제대로 사용되어지지 못했었다(GET방식, POST방식외에도 다른 방식들이 많았지만 사용하지 않았다)
그래서 REST를 만듦

1. 구성  
 (1) 자원 - URI   
   /board/read?{bno=123}  
   ( URI, URL )?(파라미터)  
   get은 파라미터를 QueryString를 사용하여 보내고 post는 HTTP body에 담아서 보낸다.  
   왜 파라미터를 뒤에 붙이냐?? 에서 나온것이 REST API (/board/read/123)  
   멱등 : 누가 어디서 보든 같은것을 보아야 한다.  
 (2) 행위 - HTTP METHOD  
 (3) 표현
 
 2. 특징
   1) Uniform (유니폼 인터페이스)  
   2) Stateless (무상태성)  
   3) Cacheable (캐시 가능) 
   4) Self-descriptiveness (자체 표현 구조)  
   5) Client - Server 구조  
   6) 계층형 구조  

3. REST API디자인
   1) REST API는 리소스 명이 명사이며 s가 붙는다  
   2) http method가 GET, POST, PUT, DELETE등으로 표현된다  
       (1) get : 조회를 할 때  
       (2) post : 추가를 할 때  
       (3) put : 수정을 할 때  
       (4) delete : 삭제 할 때  
   3) 연관 관계가 있을 때  
      GET : /users/{userid}/devices   
      
4. HTTP응답 상태 코드
![image](https://user-images.githubusercontent.com/72544949/111240844-5a3de700-863f-11eb-8b51-34dc3df8287c.png)

5. url사용
![image](https://user-images.githubusercontent.com/72544949/111241014-9f621900-863f-11eb-9526-64d0d5e64e1f.png)  
![image](https://user-images.githubusercontent.com/72544949/111241117-d2a4a800-863f-11eb-899c-84844b079c3b.png)  

위의 사진은 예시이며 개발자마다 방식이 조금 다르다.

6. HATEOAS도입
전형적인 REST API응답 데이터는
```
{
  "name": "jun"
}
```

```
{
  "name": "jun",
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8080/user"
    },
    {
      "rel": "delete",
      "href": "http://localhost:8080/user/delete"
    },
    {
      "rel": "update",
      "href": "http://localhost:8080/user/update"
    }
  ]
}
```

7. 어노테이션
 1) @RestController : REST방식 컨트롤러 ResponseBody가 자동으로 들어감
 2) @RestponseBody : 뷰로 전달하는 것이 아니라 데이터 자체를 전달
 3) @PathVariable : URL 경로에 있는 값을 파라미터로 추출
 4) @CrossOrigin : json cors처리
 5) @RequestBody : Json Xml데이터 처리

* 맛보기 코드

1. /domain/SampleDTO.java 
```
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleDTO {

	private Integer sno;
	private String first;
	private String last;
}
```

2. /controller/SampleController.java
```
@RestController
@Log4j
@RequestMapping("/sample")
public class SampleController {
  
	//consumes 받을때 어떤형식만 받겠다 produces 어떤 형태만 만들겠다. (두가지를 사용하면 엄격해진다)
	@GetMapping(value = "/get1", produces = {MediaType.APPLICATION_JSON_VALUE} )
	public ResponseEntity<SampleDTO> getSample() {
		
		SampleDTO result = new SampleDTO(11,"노","승원");
		
		return new ResponseEntity<SampleDTO>(result, HttpStatus.OK);
	}
}
```  
![image](https://user-images.githubusercontent.com/72544949/111245145-83fb0c00-8647-11eb-835b-3b757ba43068.png)

3. SampleController.java (POST)
```
@RestController
@Log4j
@RequestMapping("/samples")
public class SampleController {
	
	@PostMapping("")
	public ResponseEntity<SampleDTO> postSample(@RequestBody SampleDTO dto){
		
		log.info(dto);
		
		return new ResponseEntity<SampleDTO>(dto, HttpStatus.OK);
	}
```  
![image](https://user-images.githubusercontent.com/72544949/111246155-1f40b100-8649-11eb-91fd-b8e2fd1ad8f3.png)  
![image](https://user-images.githubusercontent.com/72544949/111246163-249dfb80-8649-11eb-8af7-ac0bdcb71e98.png)  

4. @PathVariable
```
@RestController
@Log4j
@RequestMapping("/samples")
public class SampleController {
	
	@PostMapping("/{cat}")
	public ResponseEntity<SampleDTO> postSample(
			@PathVariable(name ="cat") Long cat, 
			@RequestParam(name="perSheet") Integer perSheet,
			@RequestBody SampleDTO dto){
		
		log.info("cat: " +cat);
		log.info("perSheet: " + perSheet);
		log.info(dto);
		
		return new ResponseEntity<SampleDTO>(dto, HttpStatus.OK);
	}
```  
![image](https://user-images.githubusercontent.com/72544949/111247812-05ed3400-864c-11eb-9d3d-33f30c1bcc29.png)  
![image](https://user-images.githubusercontent.com/72544949/111247799-fff75300-864b-11eb-8aa1-f6f0c4ba3e8b.png)

5. @DeleteMapping
```
@RestController
@Log4j
@RequestMapping("/samples")
public class SampleController {
	
	@DeleteMapping("/{sno}")
	public ResponseEntity<Boolean> removeSample(@PathVariable(name = "sno") Integer sno){
		
		log.info("SNO: " + sno);
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}
```
![image](https://user-images.githubusercontent.com/72544949/111248126-a4799500-864c-11eb-99f0-ec21a50790a2.png)  
![image](https://user-images.githubusercontent.com/72544949/111248133-a9d6df80-864c-11eb-9362-b1d3dc480fce.png)  

6. @PutMapping
```
@RestController
@Log4j
@RequestMapping("/samples")
public class SampleController {
	
	@PutMapping("/{sno}")
	public ResponseEntity<SampleDTO> modify( @RequestBody SampleDTO dto){
		
		log.info("modify.....................");
		log.info(dto);
		
		return new ResponseEntity<SampleDTO>(dto, HttpStatus.OK);
	}
```
![image](https://user-images.githubusercontent.com/72544949/111248538-79437580-864d-11eb-99b3-066a887f4787.png)  
![image](https://user-images.githubusercontent.com/72544949/111248544-7e082980-864d-11eb-8dfd-8a8fa014712c.png)  



