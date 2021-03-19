package org.clazh.security;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j
public class SecurityTests {

    @Autowired
    private PasswordEncoder pwEncoder;

    @Test
    public void test1() {
        log.info("test");
    }

    @Test
    public void testMember() {

        String pw = "member";

        String enPW = pwEncoder.encode(pw);

        log.info(enPW);
    }
    
    @Test
    public void testMatch() {
    	String secret = "$2a$10$sopMQgBXCckrppYyLHGCEexuYLDJIDk5KfNZcBBqaz6jMNXGYHlS2";
    	
    	boolean result = pwEncoder.matches("member", secret);

    	log.info(result);
    }
}
