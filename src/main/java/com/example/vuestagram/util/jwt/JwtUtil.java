package com.example.vuestagram.util.jwt;

import com.example.vuestagram.model.User;
import com.example.vuestagram.util.jwt.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component // 클래스 전체를 Bean 등록
public class JwtUtil {
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;

	public JwtUtil(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;

		// Base64 인코딩된 비밀키를 디코딩해 키 객체로 변환
		// HAMC 서명에 필요한 적절한 비밀키를 제공하기 위한 것
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
	}

	// 액서스 토큰 생성
	public String generateAccessToken(User user) {
		return this.generateToken(user.getUserId(), jwtConfig.getAccessTokenExpiry());
	}

	// 리프래시 토큰 생성
	public String generateRefreshToken(User user) {
		return this.generateToken(user.getUserId(), jwtConfig.getRefreshTokenExpiry());
	}

	// 토큰 생성
	public String generateToken(Long userId, int expiry) {
		Date now = new Date(); // 현재 시간

		return Jwts.builder()
						.header().type(jwtConfig.getType())
						.and()
						.setSubject(String.valueOf(userId))
						.setIssuer(jwtConfig.getIssuer())
						.setIssuedAt(now)
						.setExpiration(new Date(now.getTime() + expiry))
						.signWith(secretKey)
						.compact();
	}
}
