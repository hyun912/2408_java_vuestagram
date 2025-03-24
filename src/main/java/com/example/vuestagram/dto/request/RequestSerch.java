package com.example.vuestagram.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSerch {
	// 검색기능 사용할 때 이용할 DTO

	@Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣._@-]{1,50}$", message = "부적절한 검색어 입니다.")
	private String content;

	@Pattern(regexp = "^[0-9]+$", message = "부적절한 검색어 입니다.")
	private long userId;
}
