package com.example.vuestagram.util.jwt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

// @ 치는걸 어노테이션
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "config.jwt")
public class JwtConfig {
	private final String issuer; // 회사 대표 이메일
	private final String type; // 토큰 타입
	private final int accessTokenExpiry; // 토큰 유효 시간, ms 단위
	private final int refreshTokenExpiry; // 리프레쉬 유효 시간, ms 단위
	private final String refreshTokenCookieName; // 저장 쿠키 이름
	private final int refreshTokenCookieExpiry; // 쿠키 유효 시간, ms 단위
	private final String secret; // 암호화 번호
	private final String headerKey;
	private final String scheme;
	private final String reissUri;

}
