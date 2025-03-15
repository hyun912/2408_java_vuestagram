package com.example.vuestagram.dto.response;

import com.example.vuestagram.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseLogin {
	private String accessToken;
	private User user;
}
