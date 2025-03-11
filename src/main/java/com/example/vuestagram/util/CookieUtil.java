package com.example.vuestagram.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {
	// Request Header에서 특정 쿠키 획득
	public Cookie getCookie(HttpServletRequest request, String name) {
		// 쿠키가 없는 경우도 있으므로 null을 허용하는 쿠키를 스트림객체로 받아옴
		return Arrays.stream(Optional
						.ofNullable(request.getCookies())
						.orElseThrow(() -> new RuntimeException("쿠키를 찾을 수 엄숴.")) // catch
						) // Stream<Cookie[]> 생성
						.filter(item -> item.getName().equals(name)) // 조건에 맞는 Stream 리턴 (중간 연산)
						.findFirst() // 필터된 아이템중 가장 위에걸 가져옴 (Optional 타입, 최종 연산)
						.orElseThrow(() -> new RuntimeException("쿠키를 찾을수 엄숴요."));
	}

	// Response Header 쿠키 세팅
	public void setCookie(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(domain); // 특정 요청만 쿠키 발급
		cookie.setMaxAge(expiry); // 만료 시간
		cookie.setHttpOnly(true); // 보안 쿠키 설정, 프론트에서 js 쿠키 획득 불가능
		response.addCookie(cookie);
	}
}
