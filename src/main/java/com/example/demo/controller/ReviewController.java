package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.entity.Review;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ReviewRepository;

@Controller
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MyAccount myAccount;

	// レビュー投稿画面の表示
	@GetMapping("/form/{revieweeId}")
	public String showReviewForm(@PathVariable("revieweeId") Integer revieweeId, Model model) {
		Account reviewee = accountRepository.findById(revieweeId).orElse(null);
		if (reviewee == null) {
			return "error"; // ユーザーが存在しない場合
		}

		model.addAttribute("reviewee", reviewee);
		return "review/review_form";
	}

	// レビュー投稿の処理
	@PostMapping("/submit")
	public String submitReview(@RequestParam("revieweeId") Integer revieweeId,
			@RequestParam("score") Short score,
			@RequestParam("reviewText") String reviewText) {

		Account reviewer = accountRepository.findById(myAccount.getId()).orElse(null);
		Account reviewee = accountRepository.findById(revieweeId).orElse(null);

		if (reviewer == null || reviewee == null) {
			return "error";
		}

		Review review = new Review();
		review.setReviewer(reviewer);
		review.setReviewee(reviewee);
		review.setScore(score);
		review.setReviewText(reviewText);
		review.setReviewDate(LocalDateTime.now());

		reviewRepository.save(review);

		return "redirect:/mypage/user/" + revieweeId + "/detail";
	}
}
