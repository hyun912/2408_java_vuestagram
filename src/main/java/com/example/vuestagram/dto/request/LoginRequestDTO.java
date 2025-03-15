package com.example.vuestagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
	@NotBlank(message = "계정은 필수입니다.")
	private String account;

	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;
}
