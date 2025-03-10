package com.example.vuestagram.controller;

import com.example.vuestagram.service.AuthService;
import com.example.vuestagram.util.jwt.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 레스트풀 API 컨트롤러
@RequestMapping("/api") // 공통 경로 설정
@RequiredArgsConstructor // 자동 DI
public class AuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public String login() {
		return authService.login();
	}

	@GetMapping("/test")
	public String test() {
		return "test method";
	}
}
