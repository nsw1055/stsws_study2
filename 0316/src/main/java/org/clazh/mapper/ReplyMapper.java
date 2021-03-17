package org.clazh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.clazh.domain.ReplyVO;

public interface ReplyMapper {

	@Insert("insert into tbl_reply (bno , reply , replyer) values (#{bno} , #{reply} , #{replyer})")
	public void insert(ReplyVO vo);
	@Select("select * from tbl_reply where rno = #{rno}")
	public ReplyVO selectOne(Integer rno);
	@Update("update tbl_reply set reply = #{reply} , updateDate = now() where rno = #{rno}")
	public void update(ReplyVO vo);
	@Delete("delete from tbl_reply where rno = #{rno}")
	public void delete(Integer rno);
	@Select("select * from tbl_reply where bno = #{bno} order by rno asc limit 0 , 10")
	public List<ReplyVO> selectList(@Param("bno") Integer bno );
}
