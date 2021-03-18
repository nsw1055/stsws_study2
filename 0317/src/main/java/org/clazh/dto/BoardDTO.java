package org.clazh.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDTO {

    private Integer bno;
    private String title, content,writer;

    private List<AttachFileDTO> fileList;

}
