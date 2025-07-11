package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Account;
import com.example.demo.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 特定のユーザーが受け取ったレビュー一覧
	List<Review> findByReviewee(Account reviewee);

	// 特定のユーザーが書いたレビュー一覧
	List<Review> findByReviewer(Account reviewer);

	// 特定のレビュアーが特定のレビューイーに書いたレビューがあるか確認（重複防止など）
	boolean existsByReviewerAndReviewee(Account reviewer, Account reviewee);

	@Query("SELECT AVG(r.score) FROM Review r WHERE r.reviewee.id = :userId")
	Double findAverageScoreByUserId(@Param("userId") Long userId);
}
