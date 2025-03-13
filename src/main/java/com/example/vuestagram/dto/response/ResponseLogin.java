package com.example.vuestagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseLogin {
	private String accessToken;
	private Long userId;
	private String name;
	private String account;
	private String profile;
}
