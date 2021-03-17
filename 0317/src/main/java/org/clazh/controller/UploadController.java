package org.clazh.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.clazh.dto.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@RestController
@Log4j
public class UploadController {
	
	@GetMapping("/view")
	public ResponseEntity<byte[]> viewFile(String file){
		
		byte[] result = null;
		
		ResponseEntity<byte[]> res = null;
		
		try {
			String deStr = URLDecoder.decode(file, "UTF-8");
			
			String originStr = deStr.replace("#",".");
			
			log.info(originStr);
			
			File targetFile = new File("C:\\upload\\"+ originStr);
			
			log.info(targetFile.toPath());
			
			//MIME 
			String mimeType = Files.probeContentType(targetFile.toPath());
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type",mimeType);
			
			res = new ResponseEntity<>(
					FileCopyUtils.copyToByteArray(targetFile),
					header,
					HttpStatus.OK
					);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
	}

	@PostMapping("/upload")
	public ResponseEntity<List<AttachFileDTO>> upload(@RequestParam("files") MultipartFile[] files){

		String uploadFolder = "C:\\upload";
		
		log.info("-------------------------");
		
		String folderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, folderPath);

		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		//null이나 length체크 
		List<AttachFileDTO> list = new ArrayList<>();
		
		for(int i = 0; i < files.length; i++) {
			MultipartFile mfile = files[i];
			
			String fileName = mfile.getOriginalFilename();
			
			UUID uuid = UUID.randomUUID();
			
			boolean isImage = mfile.getContentType().startsWith("image");
			
			File saveFile = new File(uploadPath, uuid.toString()+"_"+fileName);
			
			try {
				mfile.transferTo(saveFile);
				
				if(isImage) {
					
					FileOutputStream fos = new FileOutputStream(
							new File(uploadPath, "s_"+uuid.toString()+"_"+fileName));
					
					Thumbnailator.createThumbnail(mfile.getInputStream(), 
							fos, 100, 100);
					fos.close();
				}
				
				AttachFileDTO attachFileDTO  = 
						new AttachFileDTO(fileName, folderPath, uuid.toString(), isImage);
				
				list.add(attachFileDTO);
				
			} catch (Exception e) {

				e.printStackTrace();
			} 
			
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String str = sdf.format(new Date());
		
		return str.replace("-", File.separator);
	}
	
}

