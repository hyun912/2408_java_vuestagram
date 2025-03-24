package com.example.vuestagram.util;

import com.example.vuestagram.config.FileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUtil {
	private final FileConfig fileConfig;

	// 파일명에서 파일 확장자 반환
	public String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	// 랜덤 파일명(확장자 X) 리턴
	public String makeRandomFileName() {
		return UUID.randomUUID().toString();
	}

	// originalFileName 에서 랜덤 파일명(확장자 포함) 리턴
	public String makeRandomFileName(String originalFileName) {
		return makeRandomFileName() + getExtension(originalFileName);
	}

	// MultipartFile 객체에서 랜덤 파일명(확장자 포함) 리턴
	public String makeRandomFileName(MultipartFile file, String kindsPath) {
		String originalFileName = file.getOriginalFilename(); // Origin Name 획득
		return kindsPath + "/" + makeRandomFileName(originalFileName);
	}

	// 디렉토리 생성
	public void makeDir(String middlePath) {
		File dir = new File(fileConfig.getRealFilePath(), middlePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}

	// 파일을 해당 경로(절대주소)에 저장
	public void saveFile(MultipartFile file, String filePath) throws IOException {
		File dir = new File(fileConfig.getRealFilePath(), filePath);
		file.transferTo(dir);
	}
}
