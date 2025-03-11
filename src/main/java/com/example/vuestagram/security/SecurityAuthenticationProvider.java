package com.example.vuestagram.security;

import com.example.vuestagram.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationProvider {
	private final JwtUtil jwtUtil;

	// Spring Security에서 인증 정보를 담는 객체 생성
	public Authentication authenticate(String token) {
		// 인증된 사용자 객체, 비밀번호 저장 여부, 사용자 권한 목록
		return new UsernamePasswordAuthenticationToken(jwtUtil.getClaims(token), null, null);
	}
}
