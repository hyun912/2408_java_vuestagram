package com.example.vuestagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// setter는 일일히 하나씩 넣어야하는 반면에
// builder는 체이닝으로 한번에 넣고 마지막에 build()만 헤주면 됨
public class ResponseBase<T> {
	private String message;
	private int status;
	private T data;
}
