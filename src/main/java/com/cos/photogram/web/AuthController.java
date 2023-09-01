package com.cos.photogram.web;


import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogram.domain.user.User;
import com.cos.photogram.service.AuthService;
import com.cos.photogram.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI 할때 사용
@Controller // 1.IoC 2.파일을 리턴하는 컨트롤러
public class AuthController {

//	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}

	@GetMapping("/auth/signin")
	public String signinForm() {
		return "/auth/signin";
	}

	@GetMapping("/auth/signup")
	public String signupForm() {
		return "/auth/signup";
	}

	// 회원가입버튼 -> /auth/signup -> /auth/signin
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { // key=value (x-www-form-urlencoded)

//			log.info(signupDto.toString());
		// User <- SignupDto
		User user = signupDto.toEntity();
		authService.Join(user);
//			System.out.println(userEntity);
		return "/auth/signin";
	}

}
