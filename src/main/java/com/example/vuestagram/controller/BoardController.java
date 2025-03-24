package com.example.vuestagram.controller;

import com.example.vuestagram.dto.request.RequestBoardStore;
import com.example.vuestagram.dto.request.RequestSerch;
import com.example.vuestagram.model.Board;
import com.example.vuestagram.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
	private final BoardService boardService;

	@GetMapping
	public ResponseEntity<Page<Board>> paginateBoard(Pageable pageable) {
		// Pageable 객체를 이용하여 쉽게 페이지네이션 구현
		// 쿼리 파라미터로 page, size, sort를 넘겨주면 자동으로 처리
		// page : 0부터 시작
		// size : 한 페이지당 출력 수
		// sort : 정렬 기준
		// 예) http://localhost:8080/api/boards?page=0&size=5&sort=createdAt,DESC
		return ResponseEntity.status(200).body(boardService.paginateBoards(pageable));
	}

	@GetMapping("/{boardId}")
	// @Min(1) : 최소값 1이상인지 체크
	// @PathVariable : 세그먼트 파라미터 획득, name속성값을 세그먼트 파라미터명으로 지정하여 바인딩
	public ResponseEntity<Board> show(@Min(1) @PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.status(200).body(boardService.show(boardId));
	}

	@PostMapping
	public ResponseEntity<Board> store (
					// @RequestPart : Form Data와 함께 JSON Data를 함께 이용할 경우 사용
					// name속성을 통해 요청에서 사용한 키를 지정
					// 파일 데이터는 별도로 받고, 기타 유저 데이터는 JSON으로 받음
					@Valid @RequestPart(name = "json") RequestBoardStore requestBoardStore
					,@RequestPart(name="img", required = false) MultipartFile file
					,HttpServletRequest request
	) {
		return ResponseEntity.status(200).body(boardService.store(requestBoardStore, file, request));
	}

	@GetMapping("/search")
	public ResponseEntity<Page<Board>> search(
					@ModelAttribute RequestSerch requestSerch
					, Pageable pageable
	) {
		// Pageable 객체에서 자동으로 관리하는 page, size, sort 등 외의 파라미터도 존재할 경우 DTO로 별도 관리 가능
		// 아래와 같이 요청이 온다면 page, size, sort는 Pageable객체에 담기고,
		// content, userId는 해당 DTO에 담김
		// http://localhost:8080/api/boards/search?content=0&userId=13&page=1&size=5&sort=likes,ASC&sort=createdAt,DESC
		return ResponseEntity.status(200).body(boardService.paginateSerch(pageable, requestSerch));
	}
}
