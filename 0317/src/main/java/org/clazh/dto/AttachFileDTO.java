package org.clazh.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileDTO {

	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
	
	public String getLink() {


		String fileLinkName = fileName.replace(".", "#");
		//2021/03/17
		String str = uploadPath+"/"+uuid+"_"+fileLinkName;

		String enStr = "";

		try {
			enStr = URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return enStr;
	}

	public String getThumbLink() {


		String fileLinkName = fileName.replace(".", "#");
		//2021/03/17
		String str = uploadPath+"/s_"+uuid+"_"+fileLinkName;

		String enStr = "";

		try {
			enStr = URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return enStr;
	}
}