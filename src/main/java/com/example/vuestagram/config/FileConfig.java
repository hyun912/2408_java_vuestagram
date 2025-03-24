package com.example.vuestagram.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "config.file")
@RequiredArgsConstructor
@ToString
public class FileConfig {
	// 이미지 파일을 저장한 공통 경로(절대경로로 설정됨)
	// ex) D:/workspace/2408_java_vuestagram/src/main
	private final String realFilePath;

	// 프로필 파일을 저장할 중간 경로
	// ex) /img/profile
	private final String profilePath;

	// 게시글 파일을 저장할 중간 경로
	// ex) /img/board
	private final String boardImgPath;
}
