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
   
