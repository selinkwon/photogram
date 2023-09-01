package com.cos.photogram.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.user.User;
import com.cos.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		Map<String, Object> userInfo = oAuth2User.getAttributes();

		String userName = "google_"+(String) userInfo.get("id");
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
		String email = (String) userInfo.get("email");
		String name = (String) userInfo.get("name");


		User userEntity = userRepository.findByUsername(userName);

		if(userEntity == null) { // 페이스북 최초 로그인
			User user = User.builder()
					.username(userName)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();


			return new PrincipalDetails(userRepository.save(user), oAuth2User.getAttributes());
		}else { // 페이스북으로 이미 회원가입이 되어있음
			return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
		}


		// 페이스북으로 로그인했는지 구분 할 필요가 없다면 oAuth2User.getAttributes() 안넣어줘도 됨! 구분용!

	}
}
