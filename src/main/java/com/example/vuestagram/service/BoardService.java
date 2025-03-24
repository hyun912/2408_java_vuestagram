package com.example.vuestagram.service;

import com.example.vuestagram.config.FileConfig;
import com.example.vuestagram.dto.request.RequestBoardStore;
import com.example.vuestagram.dto.request.RequestSerch;
import com.example.vuestagram.model.Board;
import com.example.vuestagram.model.QBoard;
import com.example.vuestagram.model.User;
import com.example.vuestagram.repogitory.BoardRepogitory;
import com.example.vuestagram.repogitory.UserRepogitory;
import com.example.vuestagram.util.FileUtil;
import com.example.vuestagram.util.OrderSpecifierUtil;
import com.example.vuestagram.util.jwt.JwtUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepogitory boardRepogitory;
	private final UserRepogitory userRepogitory;
	private final JPAQueryFactory queryFactory;
	private final JwtUtil jwtUtil;
	private final FileUtil fileUtil;
	private final FileConfig fileConfig;


	// 페이지네이션 처리
	public Page<Board> paginateBoards(Pageable pageable) {
		// Pageable 객체를 이용해 페이지네이션 처리
		// 리턴 타입은 Page<T>
		return boardRepogitory.findAll(pageable);
	}

	// 디테일 처리
	public Board show(long boardId) {
		// PK로 게시글 검색, 해당 게시글이 없을 시 RuntimeException Throw
		return boardRepogitory.findById(boardId).orElseThrow(() -> new RuntimeException("삭제 된 게시글 입니다."));
	}

	// 작성
	@Transactional // 트랜잭션 처리
	public Board store(RequestBoardStore requestBoardStore, MultipartFile file, HttpServletRequest request) {
		// 엑세스 토큰에서 유저정보 획득
		String accessToken = jwtUtil.getAccessTokenInCookie(request);
		Claims claims = jwtUtil.getClaims(accessToken);

		// 작성 유저 체크
		// `orElseThrow()`는 값이 존재하지 않으면 예외를 던짐
		User user = userRepogitory.findById(Long.parseLong(claims.getSubject())).orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));

		// 이미지 경로 획득
		// 여기서는 파일 저장경로만 생성
		// board 데이터를 먼저 insert 후, 실제 파일 저장 처리 진행
		// 프로필 패스는 `중간경로 + 파일명`으로 이루어짐
		// ex) /img/boards/34wj6hgkj346fv.png
		String imgPath = "";
		if(file != null && file.getSize() > 0) {
			imgPath = fileUtil.makeRandomFileName(file, fileConfig.getBoardImgPath());
		}
		// String imgPath = file != null ? fileUtil.makeRandomFileName(file, fileConfig.getBoardImgPath()) : "";

		// board 엔티티 생성
		Board board = new Board();
		board.setContent(requestBoardStore.getContent());
		board.setUser(user); // Relation을 맺어뒀으므로 위에서 가져온 작성유저 엔티티를 사용
		board.setImg(imgPath);
		board.setLike(0);
		boardRepogitory.save(board);

		try {
			if(file != null) {
				fileUtil.makeDir(fileConfig.getBoardImgPath()); // 파일 경로 생성 (파일 경로 없는 상태에서 파일 업로드시 에러 발생)
				fileUtil.saveFile(file, imgPath); // 파일 저장 처리
			}
		} catch (Exception e) {
			throw new RuntimeException("파일 저장 에러 발생: " + e.getMessage());
		}

		return board;
	}

	// 검색어가 포함 된 페이지네이션 처리(동적 쿼리 예제)
	public Page<Board> paginateSerch(Pageable pageable, RequestSerch requestSerch) {
		// QueryDSL이 자동으로 생성해주는 Board Entity 기반의 QClass
		QBoard board = QBoard.board;

		// SELECT 쿼리를 작성하기 위한 JPAQuery를 생성
		JPAQuery<Board> query = queryFactory.selectFrom(board);

		// ----- 동적 쿼리 작성-----
		// content가 검색 조건에 있다면 like(포함) 필터 추가
		if(requestSerch.getContent() != null && !requestSerch.getContent().isEmpty()) {
			query.where(board.content.contains(requestSerch.getContent()));
		}

		// likes가 검색 조건에 있다면 필터 추가
		if(requestSerch.getUserId() != 0) {
			query.where(board.user.userId.eq(requestSerch.getUserId()));
		}
		// -----------------------

		// 전체 개수 획득
		long total = query.fetchCount();

		// 페이지네이션 데이터 획득
		List<Board> boards = query
						.orderBy(OrderSpecifierUtil.generateOrderSpecifier(pageable, board))
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.fetch();

		// Page<T> 객체로 반환하기 위해 PageImpl<>를 인스턴스
		// 각 파라미터는 페이지네이션 처리 결과, 사용한 Pageable객체, 전체 개수
		// PageImpl<>()의 제너릭은 첫번째 파라미터의 제너릭으로 지정됨
		// 아래의 경우, boards의 타입은 List<Board>이므로 PageImpl<>의 제너릭은 Board로 설정 됨
		return new PageImpl<>(boards, pageable, total);
	}
}
