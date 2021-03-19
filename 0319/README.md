* 스프링 시큐리티

스프링 시큐리티와 스프링 웹 시큐리티를 구분해 주어야 하지만 보통 시큐리티 하면 웹 시큐리티를 뜻 한다.

로그인등의 보안
1. 필터 : 서블릿 API -> 스프링과 분리되어 있음 -> 스프링에서 거의 사용하지 않음
2. 인터셉터 : 필터와 유사함 
3. 스프링 웹 시큐리티 : 비교적 최근에 나온 방식 필터와 인터셉터를 합

* 코드
1.라이브러리 추가
```
<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-core -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-core</artifactId>
			<version>5.4.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-web -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-web</artifactId>
			<version>5.4.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-config -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-config</artifactId>
			<version>5.4.2</version>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-taglibs -->
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-taglibs</artifactId>
			<version>5.4.2</version>
		</dependency>
```
2.web.xml
```
<filter>
		<filter-name>springSecurityFilterChain</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>springSecurityFilterChain</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
3.security-context.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

<security:http>
	<security:form-login/>
</security:http>
<security:authentication-manager>
</security:authentication-manager>
</beans>
```
![image](https://user-images.githubusercontent.com/72544949/111721235-cf5d2680-88a2-11eb-9f9c-8fac4045212c.png)  
자동으로 login 페이지가 생긴다.  

4. SampleController.java
```
@Controller
@RequestMapping("/sample")
@Log4j
public class SampleController {

    @GetMapping("/all")
    public void all() {

    }

    @GetMapping("/member")
    public void member() {

    }

    @GetMapping("/admin")
    public void admin() {

    }
}
```
5. jsp파일 생성
6. 접근제한 security-context
```
<security:intercept-url pattern="/sample/all" access="permitAll()"/>
<security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
```
* 동작방식
![image](https://user-images.githubusercontent.com/72544949/111724832-4f868a80-88a9-11eb-9445-8afc1d09d68d.png)  

7. 권한부여
```
	<security:authentication-provider>
		<security:user-service>
			<security:user name="member" password="member" authorities="ROLE_MEMBER"/>
		</security:user-service>
	</security:authentication-provider>
```
![image](https://user-images.githubusercontent.com/72544949/111726340-1b609900-88ac-11eb-8df0-58e2a1ccb81d.png)

8.noop
```
<security:user name="member" password="{noop}member" authorities="ROLE_MEMBER"/>
```
정상적으로 실행

9.ROLE의 이해
```
<security:http>
	<security:intercept-url pattern="/sample/all" access="permitAll()"/>
	<security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
	<security:intercept-url pattern="/sample/admin" access="hasRole('ROLE_ADMIN')"/>
	<security:form-login/>
</security:http>
<security:authentication-manager>
	<security:authentication-provider>
		<security:user-service>
			<security:user name="member" password="{noop}member" authorities="ROLE_MEMBER"/>
			<security:user name="admin" password="{noop}admin" authorities="ROLE_ADMIN,ROLE_MEMBER"/>
		</security:user-service>
	</security:authentication-provider>
</security:authentication-manager>
</beans>
```
member아이디로 admin페이지를 가면 403에러가 나온다
403 에러는 접근제한 메시지로 AccessDeniedHandler로 접근제한 페이지를 만들 수 있다.

10. AccessDeniedHandler
CommonController
```
@Controller
@Log4j
public class CommonController {

    @GetMapping("/accessError")
    public void accessDenied(Authentication auth, Model model){
        log.info("-------------------");
        log.info(auth);
        model.addAttribute("msg", "권한이 없는 사용자 입니다.");
    }

}
```
security-context
```
<security:access-denied-handler error-page="/accessError" />
```
![image](https://user-images.githubusercontent.com/72544949/111727999-50221f80-88af-11eb-8b45-9dcade050ea4.png)  


