package com.example.vuestagram.service;

import com.example.vuestagram.model.User;
import com.example.vuestagram.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtUtil jwtUtil;

	public String login() {
		User user = new User();
		user.setUserId(2L);

		// 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		return accessToken+ " || " + refreshToken;
	}
}
