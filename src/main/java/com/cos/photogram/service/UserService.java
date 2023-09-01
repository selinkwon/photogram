package com.cos.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.cos.photogram.config.auth.PrincipalDetails;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.domain.subscribe.SubscribeRepository;
import com.cos.photogram.domain.user.User;
import com.cos.photogram.domain.user.UserRepository;
import com.cos.photogram.handler.ex.CustomApiException;
import com.cos.photogram.handler.ex.CustomException;
import com.cos.photogram.handler.ex.CustomValidationApiException;
import com.cos.photogram.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public User newProfile(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();//범용고유식별자
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); // ㄴ 위같은 상황을 방지할수있음

		Path imageFilePath = Paths.get(uploadFolder+imageFileName);

		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		return userEntity;
	} // 더티체킹으로 업데이트 됨

	@Transactional(readOnly = true)
	public UserProfileDto userProfile(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();
		// SELECT * FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId); // 1은 주인 -1은 주인이 아님
		dto.setImageCount(userEntity.getImages().size());

		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);

		// 좋아요 카운트
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikeCount()); // -> getLikes().size()
		});

		return dto;
	}

	@Transactional
	public User Update(int id, User user, String password) {
		//1.영속화
		//1. 무조건찾음 get() 2. 익셉션 발동 orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(()->{
			return new CustomValidationApiException("찾을 수 없는 id입니다.");
		});

		System.out.println("변경전"+password);
		System.out.println("변경후"+user.getPassword());
		//2.영속화된 오브젝트를 수정 - 더티체킹 (업데이트 완료)
		userEntity.setName(user.getName());


			String rawPassword = user.getPassword();
			String encPassword = bCryptPasswordEncoder.encode(rawPassword);
			userEntity.setPassword(encPassword);


		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;
	}// 더티체킹이 일어나서 업데이트가 완료됨


}
