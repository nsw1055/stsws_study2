package org.clazh.controller;

import java.util.List;

import org.clazh.domain.ReplyVO;
import org.clazh.mapper.ReplyMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@CrossOrigin
@RestController
@Log4j
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyMapper mapper;
	
	@DeleteMapping("/{rno}")
	public ResponseEntity<Boolean> deleteReply(@PathVariable(name="rno") Integer rno){
		
		mapper.delete(rno);
		
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}
	
	@PostMapping("/new")
	public ResponseEntity<ReplyVO> insertReply(
			@RequestBody ReplyVO vo
			){
		
		mapper.insert(vo);
		
		return new ResponseEntity<ReplyVO>(vo,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<ReplyVO>> getReply(ReplyVO vo){
		
		List<ReplyVO> result = mapper.selectList(509);
		
		return new ResponseEntity<List<ReplyVO>>(result, HttpStatus.OK);
		
	}
}
