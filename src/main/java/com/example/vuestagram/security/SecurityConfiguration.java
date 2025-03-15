package com.example.vuestagram.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 설정을 활성화, 5.7버전 이상에서는 생략 가능
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final TokenAuthenticationFilter tokenAuthenticationFilter;

	@Bean
	// 비밀번호 암호화 관련 구현체 정의 및 빈등록
	public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
						.httpBasic(h -> h.disable()) // SSR이 아니므로 화면 생성 비활성 설정
						.formLogin(form -> form.disable()) // SSR이 아니므로 폼로그인 기능 비활성 설정
						.csrf(csrf -> csrf.disable()) // SSR이 아니므로 CSRF 토큰 인증 비활성 설정
						.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 인가 처리 전 스프링 시큐리 인증 필터 실행
						.authorizeHttpRequests(request -> // 리퀘스트에 대한 인가 체크 처리
										request.requestMatchers("/api/login").permitAll() // `/api/login`은 인가 없이 접근 가능
														.anyRequest().authenticated() // 위에서 정의한 것들 이 외에는 인가 필요
						)
						.build();
	}
}
