package org.clazh.controller;

import org.clazh.domain.SampleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/samples")
public class SampleController {
	
	@PutMapping("/{sno}")
	public ResponseEntity<SampleDTO> modify( @RequestBody SampleDTO dto){
		
		log.info("modify.....................");
		log.info(dto);
		
		return new ResponseEntity<SampleDTO>(dto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{sno}")
	public ResponseEntity<Boolean> removeSample(@PathVariable(name = "sno") Integer sno){
		
		log.info("SNO: " + sno);
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}
	
	@PostMapping("/{cat}")
	public ResponseEntity<SampleDTO> postSample(
			@PathVariable(name ="cat") Long cat, 
			@RequestParam(name="perSheet") Integer perSheet,
			@RequestBody SampleDTO dto){
		
		log.info("cat: " +cat);
		log.info("perSheet: " + perSheet);
		log.info(dto);
		
		return new ResponseEntity<SampleDTO>(dto, HttpStatus.OK);
	}
  
	//consumes 받을때 어떤형식만 받겠다 produces 어떤 형태만 만들겠다. (두가지를 사용하면 엄격해진다)
	@GetMapping(value = "/get1", produces = {MediaType.APPLICATION_JSON_VALUE} )
	public ResponseEntity<SampleDTO> getSample() {
		
		SampleDTO result = new SampleDTO(11,"노","승원");
		
		return new ResponseEntity<SampleDTO>(result, HttpStatus.OK);
	}
}
