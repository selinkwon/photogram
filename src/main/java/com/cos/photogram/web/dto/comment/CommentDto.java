package com.cos.photogram.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

// NotNull = Null값 체크
// NotEmpty = 빈값이거나 null 체크
// NotBlank = 빈값이거나 null , 빈공백(스페이스) 체크

@Data
public class CommentDto {
	
	@NotBlank
	private String content;
	@NotNull
	private int imageId;
	
	// toEntity가 필요없음 -> why?
}
