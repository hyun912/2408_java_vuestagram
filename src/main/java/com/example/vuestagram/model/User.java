package com.example.vuestagram.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private Long userId;
	private String account;
	private String password;
	private String name;
	private String profile;
	private String gender;
	private String refreshToken;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
