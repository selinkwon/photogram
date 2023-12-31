package com.cos.photogram.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogram.domain.user.User;
import com.cos.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service //IoC
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	// 패스워드는 알아서 체킹한다.
	// 리턴이 잘되면 자동으로 UserDetails타입을 세션으로 만든다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			return null;
		}else {
			return new PrincipalDetails(userEntity);
		}		
		
	}

	
}
