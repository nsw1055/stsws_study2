package org.clazh.service;

import org.clazh.mapper.SampleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor	
@Transactional
public class SampleServicImpl implements SampleService {

	private final SampleMapper mapper;

	@Override
	public String doA() {
		log.info("doA..............");
		
		String str = "동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리 나라만세 무궁화 삼천리 화려강산 대한사랑 대한으로 길이 보전하세";
		
		mapper.insert1(str);
		mapper.insert2(str);
		
		return "doA";
	}
	
	@Override
	public String doB() {
		log.info("doB..............");
		
		mapper.insert3("AAA", "BBB", "CCC");
		
		log.info("======================");
		log.info(mapper.getLast());
		log.info("======================");
		return "doB";
	}

	@Override
	public String doC() {
		log.info("doC..............");
		return "doC";
	}
}
