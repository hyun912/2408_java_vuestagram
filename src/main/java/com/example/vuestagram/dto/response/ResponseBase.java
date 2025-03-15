package com.example.vuestagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseBase<T> {
	private String message;
	private int status;
	private T data;
}
