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

@Entity
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// 書いた人（レビュワー）
	@ManyToOne
	@JoinColumn(name = "reviewer_id", nullable = false)
	private Account reviewer;

	// 書かれた人（レビュイー）
	@ManyToOne
	@JoinColumn(name = "reviewee_id", nullable = false)
	private Account reviewee;

	@Column(nullable = false)
	private Short score;

	@Column(name = "review_text")
	private String reviewText;

	@Column(name = "review_date", nullable = false)
	private LocalDateTime reviewDate = LocalDateTime.now();

	// --- Getter / Setter ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getReviewer() {
		return reviewer;
	}

	public void setReviewer(Account reviewer) {
		this.reviewer = reviewer;
	}

	public Account getReviewee() {
		return reviewee;
	}

	public void setReviewee(Account reviewee) {
		this.reviewee = reviewee;
	}

	public Short getScore() {
		return score;
	}

	public void setScore(Short score) {
		this.score = score;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public LocalDateTime getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(LocalDateTime reviewDate) {
		this.reviewDate = reviewDate;
	}

	public Review() {
	} // JPA用
}
