package com.example.vuestagram.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegistration {
	@NotBlank(message = "아이디 필수") // null, 빈문자열(""), 공백만 있는 문자열(" ")이면 안됨
	private String account;

	@NotBlank(message = "비밀번호 필수")
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{5,20}$", message = "비밀버놓 양식 틀림") // 정규식 이용
	private String password;

	@NotBlank(message = "이름 필수")
	@Pattern(regexp = "^[가-힣]{2,50}$", message = "이름 한글만 가능") // 정규식 이용
	private String name;

	@NotBlank(message = "성별 필수")
	@Pattern(regexp = "^[01]$", message = "성별 입력 이상") // 정규식 이용
	private String gender;
}
