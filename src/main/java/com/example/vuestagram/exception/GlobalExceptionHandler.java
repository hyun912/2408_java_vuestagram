package com.example.vuestagram.exception;

import com.example.vuestagram.dto.response.ResponseBase;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ResponseBase> handleExpiredJwtException(ExpiredJwtException e) {
		log.error("JWT 토큰이 만료되었습니다." + e.getMessage());

		ResponseBase<String> result = ResponseBase.<String>builder()
						.status(400)
						.message("JWT 토큰이 만료되었습니다.")
						.build();

		return ResponseEntity.status(400)
						.body(result);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ResponseBase> handleSignatureException(SignatureException e) {
		log.error("JWT 서명이 올바르지 않습니다." + e.getMessage());

		ResponseBase<String> result = ResponseBase.<String>builder()
						.status(400)
						.message("JWT 서명이 올바르지 않습니다.")
						.build();

		return ResponseEntity.status(400)
						.body(result);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseBase<String>> handleRuntimeException(RuntimeException e) {
		log.error("런타임 에러" + e.getMessage());

		ResponseBase<String> result = ResponseBase.<String>builder()
						.status(500)
						.message(e.getMessage())
						.build();

		return ResponseEntity.status(500)
						.body(result);
	}

	//정의한 예외 이외는 Exception으로 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseBase<String>> othersHandle(Exception e) {
		log.error("예기치 못한 에러 발생" + e.getMessage());

		ResponseBase<String> result = ResponseBase.<String>builder()
						.status(500)
						.message("서버에러 발생")
						.build();

		return ResponseEntity.status(500)
						.body(result);
	}

	// 유효성 검사 실패 예외 처리 (@Valid, @Validated)
	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HandlerMethodValidationException.class})
	public ResponseEntity<ResponseBase<String>> handleValidationException(MethodArgumentNotValidException e) {
		String errorMessage = e.getBindingResult()
						.getAllErrors()
						.stream()
						.map(ObjectError::getDefaultMessage)
						.collect(Collectors.joining(", "));

		ResponseBase<String> response = ResponseBase.<String>builder()
						.status(400)
						.message("유효성 검사 실패: " + errorMessage)
						.build();

		return ResponseEntity.status(400).body(response);
	}

	// DB 에러 핸들러
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ResponseBase<String>> sqlException(SQLException e) {
		log.error("DB에러 발생" + e.getMessage());

		ResponseBase<String> result = ResponseBase.<String>builder()
						.status(500)
						.message("DB에러 발생")
						.build();

		return ResponseEntity.status(500)
						.body(result);
	}
}
