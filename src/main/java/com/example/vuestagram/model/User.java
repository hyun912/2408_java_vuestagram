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
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET updated_at = NOW, deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at IS NULL")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "account", unique = true, nullable = false, length = 20)
	private String account;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "`name`", nullable = false, length = 20)
	private String name;

	@Column(name = "profile", length = 100)
	private String profile;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "refresh_token", length = 512)
	private String refreshToken;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
