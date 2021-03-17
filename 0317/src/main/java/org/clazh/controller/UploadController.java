package org.clazh.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@RestController 
@Log4j
public class UploadController {
	
	@PostMapping("/upload")
	public ResponseEntity<String> upload(MultipartFile[] files){
		
		log.info("--------------------------");
		
		//null이나 length 체크
		for (int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];
			
			log.info(mfile.getOriginalFilename());
			log.info(mfile.getSize());
			log.info(mfile.getContentType());
		}
		
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
}
