package org.clazh.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect //로그 기능
@Component
@Log4j
public class LogAdvice {
	
	//Pointcut											메서드(..)
	@Before(value = "execution(* org.clazh..*.*Service*.*(..))")
	public void logBefore() {
		log.info("-----------------");
	}
	
	@Before(value = "execution(* org.clazh..*.*Controller*.*(..))")
	public void logBeforeController(JoinPoint jp) {
		log.info("-----------------");
		String methodName = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		
		log.info(methodName);
		log.info(Arrays.toString(args));
	}
	
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
}
