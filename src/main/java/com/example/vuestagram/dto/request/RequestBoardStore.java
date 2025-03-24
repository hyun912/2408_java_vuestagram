package com.example.vuestagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBoardStore {
	@NotBlank(message = "내용 필수")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9!@#$%^&*]{1,200}$", message = "내용 양식 틀림") // 정규식 이용
	private String content;
}
