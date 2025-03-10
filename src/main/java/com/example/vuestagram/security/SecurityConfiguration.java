package com.example.vuestagram.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 보안 설정관련 클래스
@Configuration // 여기가 설정이라는걸 알려줌
@EnableWebSecurity // 스프링 시큐리티 설정 활성화, 5.7ver 이상에선 생략가능
public class SecurityConfiguration {
	@Bean
	// 비밀번호 암호화 관련 구현체 정의 및 빈 등록
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.sessionManagement(session
			-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
		)
		.httpBasic(h -> h.disable()) // SSR이 아니므로 비활성 설정
		.formLogin(form -> form.disable()) // SSR이 아니므로 폼로그인 기능 비활성화
		.csrf(csrf -> csrf.disable()) // SSR이 아니므로 CSRF 토큰 비활성
		.authorizeHttpRequests(request -> // 리퀘스트에 대한 인가 체크
			request.requestMatchers("/api/login").permitAll() // login은 인가 없이 접근 가능
						 .anyRequest().authenticated() // 위에서 정의한 것들 이 외에는 인가 필요
		)
		.build();
	}
}
