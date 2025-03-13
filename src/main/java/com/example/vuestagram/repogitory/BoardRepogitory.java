package com.example.vuestagram.repogitory;

import com.example.vuestagram.model.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepogitory extends JpaRepository<Board, Long> {
	@EntityGraph(attributePaths = {"user"})
	Optional<Board> findById(long id);
}
