package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = { "follower_id", "followed_id" }))
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// フォローした側
	@ManyToOne
	@JoinColumn(name = "follower_id", nullable = false)
	private Account follower;

	// フォローされた側
	@ManyToOne
	@JoinColumn(name = "followed_id", nullable = false)
	private Account followed;

	@Column(name = "followed_at", nullable = false)
	private LocalDateTime followedAt = LocalDateTime.now();

	// --- Getter / Setter ---
	public Long getId() {
		return id;
	}

	public Account getFollower() {
		return follower;
	}

	public void setFollower(Account follower) {
		this.follower = follower;
	}

	public Account getFollowed() {
		return followed;
	}

	public void setFollowed(Account followed) {
		this.followed = followed;
	}

	public LocalDateTime getFollowedAt() {
		return followedAt;
	}

	public void setFollowedAt(LocalDateTime followedAt) {
		this.followedAt = followedAt;
	}
}
