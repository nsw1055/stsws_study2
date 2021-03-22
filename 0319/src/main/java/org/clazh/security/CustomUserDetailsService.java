package org.clazh.security;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.clazh.domain.MemberVO;
import org.clazh.mapper.MemberMapper;
import org.clazh.security.domain.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Log4j
public class CustomUserDetailsService implements UserDetailsService {

    @Setter(onMethod_ = {@Autowired})
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Load user By UserName: " + username);

        MemberVO vo = memberMapper.read(username);

        log.info("vo: " + vo);

        return vo== null ? null : new CustomUser(vo);
    }
}
