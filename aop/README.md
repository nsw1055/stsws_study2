# spring_AOP
---
* Aspect : 추상명사 (로그로 기능을 남기려 할 때)
* Advice-Advisor :
 1. Advice : AOP코드 
    Around Advice : 직접 대상 메서드를 호출하고 결과나 예외를 처리 할 수 있다. (ProceedingJoinPoint와 결합), 실행 전과 실행 후에 처리가 가능하다. 실제 실행은 .proceed();를 사용
 2. Advisor : PointCut으로 낙점된 메서드를 결정 한다
* Proxy
* Target : 밑의 코드의 SampleServiceImpl
* Pointcut : 후보 낙점
* JoinPoint : Target 밑에 있는 메서드 (후보)
---







* 코드
---
1. pom.xml에 라이브러리 추가
```
  <dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
			<version>${org.springframework-version}</version>
		</dependency>
    
		<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
		<dependency>
			<groupId>org.aspectj</groupId>
			<artifactId>aspectjrt</artifactId>
			<version>1.9.6</version>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
		<dependency>
			<groupId>org.aspectj</groupId>
			<artifactId>aspectjweaver</artifactId>
			<version>1.9.6</version>
		</dependency>

```
2. root-context.xml 네임스페이스 추가
aop, context, mybatis-spring, tx
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
	xsi:schemaLocation="http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring-1.2.xsd
		http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd">
    
    	<!-- Root Context: defines shared resources visible to all other web components -->
		<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```

3. service 생성
```
package org.clazh.service;

public interface SampleService {
	
	public String doA();
}
```

```
package org.clazh.service;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SampleServicImpl implements SampleService {

	@Override
	public String doA() {
		log.info("doA..............");
		return null;
	}

}
```

4. root-context.xml bean 등록
```
<context:component-scan base-package="org.clazh.service"></context:component-scan>
```

5. Advice
```
package org.clazh.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect
@Component
@Log4j
public class LogAdvice {

}
```
6. root-context.xml bean 등록
```
<context:component-scan base-package="org.clazh.aop"></context:component-scan>
```
7. Advice
```
	@Before(value = "execution(* org.clazh.service.SampleService*.*(..))")
	public void logBefore() {
		log.info("-----------------");
	}
```
Aop의 적용 화살표로 확인 가능하다.
![image](https://user-images.githubusercontent.com/72544949/111093765-108cc800-857d-11eb-80e1-b2b6140b4542.png)  
SampleServiceImpl.java
![image](https://user-images.githubusercontent.com/72544949/111094085-ca843400-857d-11eb-8251-a1568cc4c300.png)

8. test
```
public class ServiceTests {
	
	@Autowired
	SampleService service;
	
	@Test
	public void show() {
		log.info(service.getClass().getName());
		
		service.doA();
	}
}
```
Advice와 Service의 log가 찍히는것을 확인 할 수 있다.
![image](https://user-images.githubusercontent.com/72544949/111094335-70d03980-857e-11eb-8779-6d2df580a102.png)

* Around Test
1. service
```
public interface SampleService {
	
	public String doA();
	public String doB();
	public String doC();
}
```

```
public class SampleServicImpl implements SampleService {

	@Override
	public String doA() {
		log.info("doA..............");
		return "doA";
	}
	
	@Override
	public String doB() {
		log.info("doB..............");
		return "doB";
	}

	@Override
	public String doC() {
		log.info("doC..............");
		return "doC";
	}
}
```

2. test
```
@Autowired
	SampleService service;
	
	@Test
	public void show() {
		log.info(service.getClass().getName());
		
		service.doA();
		
		service.doB();
		
		service.doC();
	}
```  
  
* 호출하는데 0.3초이상 걸리면 warn을 찍기 위해 @Around를 사용
3. logAdvice
```
@Around(value = "execution(* org.clazh..*.*Service*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		
		//시큐리티 처리 코드
		log.info(pjp.getSignature());
		log.info(Arrays.toString(pjp.getArgs()));
		log.info(pjp.getThis());
		
		Object result= null;
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("end---------------------");
		return result;
	}
```

4. test
![image](https://user-images.githubusercontent.com/72544949/111096513-0d94d600-8583-11eb-8f65-f55c5629842b.png)


5. 시간 확인 코드
```
@Around(value = "execution(* org.clazh..*.*Service*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		
		//시큐리티 처리 코드		
		log.info(pjp.getSignature());
		log.info(Arrays.toString(pjp.getArgs()));
		log.info(pjp.getThis());
		
		long start = System.currentTimeMillis();
		
		Object result= null;
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("end---------------------");
		
		long end = System.currentTimeMillis();
		
		log.info(end - start);
		
		return result;
	}
```

* controller 

LogAdvice
```
	@Before(value = "execution(* org.clazh..*.*Controller*.*(..))")
	public void logBeforeController(JoinPoint jp) {
		log.info("-----------------");
		String methodName = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		
		log.info(methodName);
		log.info(Arrays.toString(args));
	}
```
---

* 트랜젝션
1. root-context.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
	xsi:schemaLocation="http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring-1.2.xsd
		http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd">

	<!-- Root Context: defines shared resources visible to all other web components -->
	<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
		<property name="driverClassName" value="com.mysql.cj.jdbc.Driver"></property>
		<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/dclass?serverTimezone=UTC"></property>
		<property name="username" value="springuser"></property>
		<property name="password" value="springuser"></property>
	</bean>

	<!-- HikariCP configuration -->
	<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
		<constructor-arg ref="hikariConfig" />
	</bean>

	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	<mybatis-spring:scan base-package="org.zerock.mapper" />

	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"></property>
	</bean>
	
	<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
	<tx:annotation-driven />
	<context:component-scan
		base-package="org.clazh.service"></context:component-scan>
	<context:component-scan
		base-package="org.clazh.aop"></context:component-scan>

</beans>
```

2. DB 테이블 셋팅
```
create table tbl_sample1(col1 varchar(500));

create table tbl_sample2(col2 varchar(50));
```

3. /mapper/SampleMapper.java (i)
```
public interface SampleMapper {

	@Insert("insert into tbl_sample1 (col1) value (#(str))")
	void insert1(String str);
	
	@Insert("insert into tbl_sample2 (col2) value (#(str))")
	void insert2(String str);	
}
```

4. 실행
![image](https://user-images.githubusercontent.com/72544949/111108091-7a1bcf00-859b-11eb-8899-fe81df9d89e1.png)  
![image](https://user-images.githubusercontent.com/72544949/111108102-8142dd00-859b-11eb-8ca2-d42742898c0e.png)  
table1에는 입력이 되었지만 table2에는 입력이 되지 않았다
이때 트랜잭션을 걸어 둘다 입력이 안되게끔 해야 한다.

5. 트랜젝션 (거는위치 메서드, 인터페이스, 클레스  @Around와 함께 쓰면 트랜젝션이 안걸리므로 조심히 써야 한다.(custom AOP를 사용하거나 Throwable로 예외를 던져야 한다.))
```
@Transactional
	@Override
	public String doA() {
		log.info("doA..............");
		
		String str = "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리 나라만세 무궁화 삼천리 화려강산 대한사랑 대한으로 길이 보전하세";
		
		mapper.insert1(str);
		mapper.insert2(str);
		
		return "doA";
	}
```
table1과 table2모두 입력이 되지 않는다.

6. mapper 추가
```
	@Insert("intsert into tbl_board (title, content, writer) values (#{title}, #{content}, #{writer})")
	void insert3(@Param("title") String title,
				@Param("content") String content,
				@Param("writer") String writer);
	
	@Select("select last_insert_id()")
	Long getLast();
```

7. service
```
@Override
	public String doB() {
		log.info("doB..............");
		
		mapper.insert3("AAA", "BBB", "CCC");
		
		log.info("======================");
		log.info(mapper.getLast());
		log.info("======================");
		return "doB";
	}
```

8. test
