package com.example.vuestagram.controller;

import com.example.vuestagram.dto.request.LoginRequestDTO;
import com.example.vuestagram.dto.response.ResponseBase;
import com.example.vuestagram.dto.response.ResponseLogin;
import com.example.vuestagram.service.AuthService;
import com.example.vuestagram.util.jwt.config.JwtConfig;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 레스트풀 API 컨트롤러
@RequestMapping // 공통 경로 설정
@RequiredArgsConstructor // 자동 DI
public class AuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ResponseBase<ResponseLogin>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
		ResponseLogin responseLogin = authService.login(loginRequestDTO, response);
		ResponseBase<ResponseLogin> responseBase = ResponseBase.<ResponseLogin>builder()
						.message("로그인 성공")
						.data(responseLogin)
						.status(200)
						.build();

		return ResponseEntity.status(200).body(responseBase);
	}

	@GetMapping("/test")
	public String test() {
		return "test method";
	}
}
