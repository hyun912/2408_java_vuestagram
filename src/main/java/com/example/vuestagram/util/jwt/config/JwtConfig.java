package com.example.vuestagram.util.jwt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "config.jwt")
public class JwtConfig {
	private final String issuer;
	private final String type;
	private final int accessTokenExpiry;
	private final int refreshTokenExpiry;
	private final String refreshTokenCookieName;
	private final int refreshTokenCookieExpiry;
	private final String secret;
	private final String headerKey;
	private final String scheme;
	private final String reissUri;
}
