package org.clazh.security;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j
public class SecurityTests {

    @Autowired
    private PasswordEncoder pwEncoder;

    @Autowired
    private DataSource ds;

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

    @Test
    public void testInsertMember() {

        String sql = "insert into tbl_member(userid, userpw, username) values (?,?,?)";

        for(int i = 0; i < 100; i++) {

            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = ds.getConnection();
                pstmt = con.prepareStatement(sql);

                pstmt.setString(2, pwEncoder.encode("pw" + i));

                if(i <80) {

                    pstmt.setString(1, "user"+i);
                    pstmt.setString(3,"일반사용자"+i);

                }else if (i <90) {

                    pstmt.setString(1, "manager"+i);
                    pstmt.setString(3,"운영자"+i);

                }else {

                    pstmt.setString(1, "admin"+i);
                    pstmt.setString(3,"관리자"+i);

                }

                pstmt.executeUpdate();

            }catch(Exception e) {
                e.printStackTrace();
            }finally {
                if(pstmt != null) { try { pstmt.close();  } catch(Exception e) {} }
                if(con != null) { try { con.close();  } catch(Exception e) {} }

            }
        }//end for
    }

    @Test
    public void testInsertAuth() {


        String sql = "insert into tbl_member_auth (userid, auth) values (?,?)";

        for(int i = 0; i < 100; i++) {

            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = ds.getConnection();
                pstmt = con.prepareStatement(sql);


                if(i <80) {

                    pstmt.setString(1, "user"+i);
                    pstmt.setString(2,"ROLE_USER");

                }else if (i <90) {

                    pstmt.setString(1, "manager"+i);
                    pstmt.setString(2,"ROLE_MEMBER");

                }else {

                    pstmt.setString(1, "admin"+i);
                    pstmt.setString(2,"ROLE_ADMIN");

                }

                pstmt.executeUpdate();

            }catch(Exception e) {
                e.printStackTrace();
            }finally {
                if(pstmt != null) { try { pstmt.close();  } catch(Exception e) {} }
                if(con != null) { try { con.close();  } catch(Exception e) {} }

            }
        }//end for
    }
}
