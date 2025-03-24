package com.example.vuestagram.service;

import com.example.vuestagram.config.FileConfig;
import com.example.vuestagram.dto.request.RequestLogin;
import com.example.vuestagram.dto.request.RequestRegistration;
import com.example.vuestagram.dto.response.ResponseLogin;
import com.example.vuestagram.model.User;
import com.example.vuestagram.repogitory.UserRepogitory;
import com.example.vuestagram.util.CookieUtil;
import com.example.vuestagram.util.FileUtil;
import com.example.vuestagram.util.jwt.JwtUtil;
import com.example.vuestagram.util.jwt.config.JwtConfig;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtUtil jwtUtil;
	private final UserRepogitory userRepogitory;
	private final PasswordEncoder passwordEncoder;
	private final CookieUtil cookieUtil;
	private final JwtConfig jwtConfig;
	private final FileUtil fileUtil;
	private final FileConfig fileConfig;

	public ResponseLogin login(RequestLogin requestLogin, HttpServletResponse response) {
		// -----
		// Optional<User> result = userRepogitory.findByAccount(loginRequest.getAccount());
		//
		// // 유저 존재 여부 체크
		// if(result.isEmpty()) {
		//     throw new RuntimeException("존재하지 않는 유저입니다.");
		// }
		//
		// // Optional 객체에서 실제 값 획득
		// // Optional 객체가 NULL이면, NoSuchElementException 발생하므로 사전 체크 필수
		// User user = result.get();
		// -----

		// -----
		// 36 ~ 47라인의 소스코드를 아래와 같이 줄여서 작성 가능
		// `orElseThrow()`는 값이 존재하지 않으면 예외를 던짐
		User user = userRepogitory.findByAccount(requestLogin.getAccount())
						.orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
		// -----

		// 비밀번호 체크
		if(!(passwordEncoder.matches(requestLogin.getPassword(), user.getPassword()))) {
			throw new RuntimeException("비밀번호가 틀렸습니다.");
		}

		// 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		// 리프래시 토큰 DB에 저장
		user.setRefreshToken(refreshToken);
		userRepogitory.save(user);

		// 리프래시 토큰 쿠키에 저장
		cookieUtil.setCookie(
						response
						,jwtConfig.getRefreshTokenCookieName()
						,refreshToken
						,jwtConfig.getRefreshTokenCookieExpiry()
						,jwtConfig.getReissUri()
		);

		return ResponseLogin.builder()
						.accessToken(accessToken)
						.user(user)
						.build();
	}

	// 회원 가입
	@Transactional // 트랜잭션 처리
	public void registration(RequestRegistration requestRegistration, MultipartFile file) {
		// 기존 가입 회원 체크
		if(userRepogitory.findByAccount(requestRegistration.getAccount()).isPresent()) {
			throw new RuntimeException("이미 가입된 회원입니다.");
		}

		// 프로필 경로 획득
		// 여기서는 파일 저장경로만 생성
		// 유저 데이터를 먼저 insert 후, 실제 파일 저장 처리 진행
		// 프로필 패스는 `중간경로 + 파일명`으로 이루어짐
		// ex) /img/profile/34wj6hgkj346fv.png
		String profilePath = (file != null && file.getSize() > 0) ? fileUtil.makeRandomFileName(file, fileConfig.getProfilePath()) : "";

		// 유저 엔티티 생성
		User newUser = new User();
		newUser.setAccount(requestRegistration.getAccount());
		newUser.setPassword(passwordEncoder.encode(requestRegistration.getPassword()));
		newUser.setProfile(profilePath);
		newUser.setGender(requestRegistration.getGender());
		newUser.setName(requestRegistration.getName());

		userRepogitory.save(newUser); // Insert 처리

		try {
			if(file != null) {
				fileUtil.makeDir(fileConfig.getProfilePath()); // 파일 경로 생성 (파일 경로 없는 상태에서 파일 업로드시 에러 발생)
				fileUtil.saveFile(file, profilePath); // 파일 저장 처리
			}
		} catch (Exception e) {
			throw new RuntimeException("파일 저장 에러 발생: " + e.getMessage());
		}
	}

	// 엑세스 토큰 재발급
	public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
		// 쿠키에서 리프래시 토큰 획득
		String requestRefreshToken = cookieUtil.getCookie(request, jwtConfig.getRefreshTokenCookieName()).getValue();

		// 클레임(페이로드) 획득
		Claims payload = jwtUtil.getClaims(requestRefreshToken);

		// 유저정보 획득
		User user = userRepogitory.findById(Long.parseLong(payload.getSubject()))
						.orElseThrow(() -> new RuntimeException("리프래시 토큰 이상"));

		// 리프래시 토큰 일치 체크
		if(!requestRefreshToken.equals(user.getRefreshToken())) {
			throw new RuntimeException("리프래시 토큰 이상");
		}

		// 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		// 리프래시 토큰 DB에 저장
		user.setRefreshToken(refreshToken);
		userRepogitory.save(user);

		// 리프래시 토큰 쿠키에 저장
		cookieUtil.setCookie(
						response
						,jwtConfig.getRefreshTokenCookieName()
						,refreshToken
						,jwtConfig.getRefreshTokenCookieExpiry()
						,jwtConfig.getReissUri()
		);

		return accessToken;
	}
}
