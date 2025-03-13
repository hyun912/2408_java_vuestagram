package com.example.vuestagram.controller;

import com.example.vuestagram.model.Board;
import com.example.vuestagram.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
	private final BoardService boardService;

	@GetMapping("/test")
	public Board test() {
		return boardService.test();
	}
}
