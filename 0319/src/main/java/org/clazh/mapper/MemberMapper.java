package org.clazh.mapper;

import org.clazh.domain.MemberVO;


public interface MemberMapper {

    MemberVO read(String userid);
}
