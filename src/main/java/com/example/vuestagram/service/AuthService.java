package com.example.vuestagram.service;

import com.example.vuestagram.dto.request.LoginRequestDTO;
import com.example.vuestagram.model.User;
import com.example.vuestagram.repogitory.UserRepogitory;
import com.example.vuestagram.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtUtil jwtUtil;
	private final UserRepogitory userRepogitory;
	private final PasswordEncoder passwordEncoder;

	public String login(LoginRequestDTO loginRequestDTO) {
		Optional<User> result = userRepogitory.findByAccount(loginRequestDTO.getAccount());

		// 유저 존재 여부 체크
		if(result.isEmpty()) {
			throw new RuntimeException("존재하지 않는 유저.");
		}

		// 파라미터: 암호화 X 번호, 암호화 O 번호
		if(!(passwordEncoder.matches(loginRequestDTO.getPassword(), result.get().getPassword()))) {
			throw new RuntimeException("비밀번호가 틀렸습니다.");
		}

		// 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(result.get());
		String refreshToken = jwtUtil.generateRefreshToken(result.get());

		return accessToken+ " || " + refreshToken;
	}
}
