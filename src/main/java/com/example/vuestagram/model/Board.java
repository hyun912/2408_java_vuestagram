package com.example.vuestagram.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@Table(name = "boards")
@SQLDelete(sql = "UPDATE boards SET updated_at = NOW(), delted_at = NOW() WHERE board_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long boardId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "content", nullable = false, length = 200)
	private String content;

	@Column(name = "img", nullable = false, length = 100)
	private String img;

	@Column(name = "`like`", nullable = false, length = 11)
	private int like;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
