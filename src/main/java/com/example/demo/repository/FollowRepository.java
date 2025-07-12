package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	// フォロー済みかどうか確認
	boolean existsByFollowerAndFollowed(Account follower, Account followed);

	// フォローしてる一覧
	List<Follow> findByFollower(Account follower);

	// フォロワー一覧
	List<Follow> findByFollowed(Account followed);

	// フォロー関係1件取得（解除用などに）
	Follow findByFollowerAndFollowed(Account follower, Account followed);

	int countByFollower(Account follower);

	int countByFollowed(Account followed);
}
