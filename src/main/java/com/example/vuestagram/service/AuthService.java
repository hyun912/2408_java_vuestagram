package com.example.vuestagram.service;

import com.example.vuestagram.dto.request.LoginRequestDTO;
import com.example.vuestagram.dto.response.ResponseLogin;
import com.example.vuestagram.model.User;
import com.example.vuestagram.repogitory.UserRepogitory;
import com.example.vuestagram.util.CookieUtil;
import com.example.vuestagram.util.jwt.JwtUtil;
import com.example.vuestagram.util.jwt.config.JwtConfig;
import jakarta.servlet.http.HttpServletResponse;
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
	private final CookieUtil cookieUtil;
	private final JwtConfig jwtConfig;

	public ResponseLogin login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
		Optional<User> result = userRepogitory.findByAccount(loginRequestDTO.getAccount());

		// 유저 존재 여부 체크
		if(result.isEmpty()) {
			throw new RuntimeException("존재하지 않는 유저입니다.");
		}

		// 비밀번호 체크
		if(!(passwordEncoder.matches(loginRequestDTO.getPassword(), result.get().getPassword()))) {
			throw new RuntimeException("비밀번호가 틀렸습니다.");
		}

		// 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(result.get());
		String refreshToken = jwtUtil.generateRefreshToken(result.get());

		// 리프래시 토큰 쿠키에 저장
		cookieUtil.setCookie(
						response
						,jwtConfig.getRefreshTokenCookieName()
						,refreshToken
						,jwtConfig.getRefreshTokenCookieExpiry()
						,jwtConfig.getReissUri()
		);

		return ResponseLogin.builder()
						.accessToken(accessToken)
						.user(result.get())
						.build();
	}
}
