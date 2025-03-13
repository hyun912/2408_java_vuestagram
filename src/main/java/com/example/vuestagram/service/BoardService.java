package com.example.vuestagram.service;

import com.example.vuestagram.model.Board;
import com.example.vuestagram.model.QUser;
import com.example.vuestagram.repogitory.BoardRepogitory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepogitory boardRepogitory;
	private final JPAQueryFactory jpaQueryFactory;

	public Board test() {
//		Optional<Board> board = boardRepogitory.findById(9L);

		// Query DSL = laravel Query Builder, JPA와는 다른 라이브러리
		QBoard qBoard = QBoard.board; // QueryDSL이 자동으로 생성해주는 Board 엔티티 기반의 클래스
		JPAQuery<Board> query = jpaQueryFactory.selectFrom(qBoard);

		if(false) {
			query.where(qBoard.boardId.eq(9L));
		}

		return query.fetchFirst();
	}
}
