package com.cos.photogram.web.dto.image;

import com.cos.photogram.domain.image.Image;
import com.cos.photogram.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.domain.image.Image;
import com.cos.photogram.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {

	private MultipartFile file;
	private String caption;
	
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}


}
