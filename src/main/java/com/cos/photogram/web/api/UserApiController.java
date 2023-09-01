package com.cos.photogram.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.user.User;
import com.cos.photogram.service.SubscribeService;
import com.cos.photogram.service.UserService;
import com.cos.photogram.web.dto.CMRespDto;
import com.cos.photogram.web.dto.subscribe.SubscribeDto;
import com.cos.photogram.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {

	private final UserService userService;
	private final SubscribeService subscribeService;

	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails){
		User userEntity = userService.newProfile(principalId, profileImageFile);
		principalDetails.setUser(userEntity); // 세션 변경
		return new ResponseEntity<>(new CMRespDto<>(1,"프로필사진 변경 성공",null),HttpStatus.OK);
	}

	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){

		List<SubscribeDto> subscribeDto = subscribeService.subList(principalDetails.getUser().getId(), pageUserId);

		return new ResponseEntity<>(new CMRespDto<>(1,"구독자 정보 리스트 불러오기 성공",subscribeDto),HttpStatus.OK);
	}

	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id,
			@Valid UserUpdateDto userUpdateDto,
			BindingResult bindingResult, // 꼭 @Valid가 적혀있는 다음 파라미터에 적어야함
			@AuthenticationPrincipal PrincipalDetails principalDetails ) {

//		System.out.println("id"+id);
//		System.out.println("userEntity"+userUpdateDto.toEntity());
		User userEntity = userService.Update(id, userUpdateDto.toEntity(), principalDetails.getPassword());
		System.out.println("updatepass"+userEntity.getPassword());
		principalDetails.setUser(userEntity); // 세션 정보 변경
		return new CMRespDto<>(1, "회원수정완료", userEntity); //응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답
	}


}
