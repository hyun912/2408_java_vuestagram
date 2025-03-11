package com.example.vuestagram.security;

import com.example.vuestagram.util.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final SecurityAuthenticationProvider securityAuthenticationProvider;

	@Override
	// 액세스 토큰 유효 여부 확인하고 인증 정보를 스프링 시큐리티에 세팅
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 쿠키에서 토큰 획득
		String token = jwtUtil.getAccessTokenInCookie(request);

		if(token != null) {
			try{
				// Security 인증 세팅
				SecurityContextHolder.getContext().setAuthentication(securityAuthenticationProvider.authenticate(token));
			}catch (Exception e) {
				throw new RuntimeException("사용 못하는 토큰이양");
			}
		}

		filterChain.doFilter(request, response);
	}

}
