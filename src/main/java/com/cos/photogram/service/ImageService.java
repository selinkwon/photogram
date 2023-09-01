package com.cos.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.image.Image;
import com.cos.photogram.domain.image.ImageRepository;
import com.cos.photogram.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
	public List<Image> popularPic() {
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true) //영속성 컨텍스트 변경 감지를 해서 더티체킹, flush(반영)X
	public Page<Image> imageStory(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한것인지 비교
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public void upload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();//범용고유식별자
//		String imageFileName = imageUploadDto.getFile().getOriginalFilename(); // 실제파일명이 저장 -> 같은이름이 저장될수있음 -> 덮어씌워짐
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // ㄴ 위같은 상황을 방지할수있음
		System.out.println(imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		System.out.println(imageFilePath);
		
		// 통신, I/O -> 예외발생할수있음
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser());
		imageRepository.save(image);
		
//		System.out.println(imageEntity);
	}


}
