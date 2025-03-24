package com.example.vuestagram.controller;

import com.example.vuestagram.dto.request.RequestLogin;
import com.example.vuestagram.dto.request.RequestRegistration;
import com.example.vuestagram.dto.response.ResponseBase;
import com.example.vuestagram.dto.response.ResponseLogin;
import com.example.vuestagram.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController // REST API 컨트롤러
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<ResponseBase<ResponseLogin>> login(
					HttpServletResponse response
					,@Valid @RequestBody RequestLogin requestLogin
	) {
		ResponseLogin responseLogin = authService.login(requestLogin, response);

		ResponseBase<ResponseLogin> responseBase =
						ResponseBase.<ResponseLogin>builder()
										.status(200)
										.message("로그인 성공")
										.data(responseLogin)
										.build();

		return ResponseEntity.status(200).body(responseBase);
	}

	// 회원 가입
	@PostMapping("/registration")
	public ResponseEntity<ResponseBase<String>> registration(
					// @RequestPart : Form Data와 함께 JSON Data를 함께 이용할 경우 사용
					// name속성을 통해 요청에서 사용한 키를 지정
					// 파일 데이터는 별도로 받고, 기타 유저 데이터는 JSON으로 받음
					@Valid @RequestPart(name = "json") RequestRegistration requestRegistration
					,@RequestPart(name="profile", required = false) MultipartFile file
	) {
		authService.registration(requestRegistration, file);

		ResponseBase<String> responseBase =
						ResponseBase.<String>builder()
										.status(200)
										.message("회원가입 성공")
										.data("로그인을 해주세요.")
										.build();

		return ResponseEntity.status(200).body(responseBase);
	}

	// 엑세스 토큰 재발급
	@GetMapping("/reissue-token")
	public ResponseEntity<ResponseBase<String>> reissueToken(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = authService.reissueToken(request, response);

		ResponseBase<String> responseBase =
						ResponseBase.<String>builder()
										.status(200)
										.message("엑세스 토큰 재발급 성공")
										.data(accessToken)
										.build();

		return ResponseEntity.status(200).body(responseBase);
	}
}
