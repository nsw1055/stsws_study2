package org.clazh.mapper;


import org.clazh.domain.ReplyVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

	@Autowired
	private ReplyMapper mapper;
	
	@Test
	public void testInsertDummies() {
		int bno = 509;
		
		for(int i = 0; i < 15; i++) {
			ReplyVO vo = ReplyVO.builder().reply("test..." + i)
									      .replyer("user" + i)
										  .bno(bno)
										  .build();
			
			mapper.insert(vo);
		} // end for
	}
	
	@Test
	public void testSelect() {
		
		ReplyVO vo = mapper.selectOne(5);
		
		log.info(vo);
		
	}
	
	@Test
	public void testDelete() {

		mapper.delete(1);
		
	}
	
	@Test
	public void testUpdate() {
		
		ReplyVO vo = ReplyVO.builder().rno(3).reply("update Reply").build();
		
		mapper.update(vo);
	}
	
	@Test
	public void testList() {
		
//		mapper.selectList(509, 0).forEach(reply -> log.info(reply));
		
	}
	
	
}
