package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.entity.History;
import com.example.demo.entity.Item;
import com.example.demo.entity.Review;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReviewRepository;

@Controller
@RequestMapping("/mypage")
public class MypageController {

	@Autowired
	MyAccount myAccount;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	FollowRepository followRepository;

	// マイページ
	@GetMapping("")
	public String showMypage(Model model) {
		Account loginUser = accountRepository.findById(myAccount.getId()).orElse(null);
		if (loginUser != null) {
			int followerCount = followRepository.countByFollowed(loginUser);
			int followingCount = followRepository.countByFollower(loginUser);
			model.addAttribute("followerCount", followerCount);
			model.addAttribute("followingCount", followingCount);
			model.addAttribute("account", loginUser);
		}
		return "mypage/mypage";
	}

	@GetMapping("mypage/{id}")
	public String showMoreMypage(@PathVariable(name = "id") Long id, Model model) {

		if (id == 1) {
			List<History> orderDetail = historyRepository.findByAccountId(myAccount.getId());
			model.addAttribute("orderDetail", orderDetail);
		} else if (id == 2) {
			List<Item> itemSelling = itemRepository.findByAccountId(id);
			model.addAttribute("itemSelling", itemSelling);
		} else if (id == 3) {
			Account targetUser = accountRepository.findById(id).orElse(null);
			if (targetUser != null) {
				List<Review> myReview = reviewRepository.findByReviewee(targetUser);
				model.addAttribute("myReview", myReview);
			} else {
				model.addAttribute("myReview", List.of()); // ユーザーが見つからなかった場合は空リスト
			}
		}
		return "mypage/mypage";
	}

	// マイページ編集
	@GetMapping("/update")
	public String showMypageEditForm(Model model) {
		Account account = accountRepository.findById(myAccount.getId()).get();

		model.addAttribute("account", account);
		return "mypage/mypage_edit";
	}

	// 編集完了 →　マイページへ
	@PostMapping("/update")
	public String updateMypage(@RequestParam(value = "id", defaultValue = "") Long id,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "password", defaultValue = "") String password,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "profile", defaultValue = "") String profile,
			@RequestParam(value = "address", defaultValue = "") String address,
			@RequestParam(value = "tel", defaultValue = "") String tel) {

		Account UpdateAccount = accountRepository.findById(id).get();

		UpdateAccount.setName(name);
		UpdateAccount.setPassword(password);
		UpdateAccount.setEmail(email);
		UpdateAccount.setProfile(profile);
		UpdateAccount.setAddress(address);
		UpdateAccount.setTel(tel);

		accountRepository.save(UpdateAccount);

		return "redirect:/mypage";
	}

	// ユーザー詳細
	@GetMapping("/user/{id}/detail")
	public String showUserDetail(@PathVariable("id") Long id, Model model) {
		Account account = accountRepository.findById(id).orElse(null);
		if (account == null) {
			return "error";
		}

		List<Item> items = itemRepository.findByAccountId(id);
		List<Review> reviews = reviewRepository.findByReviewee(account);
		Double avgScore = reviewRepository.findAverageScoreByUserId(id);

		int followerCount = followRepository.countByFollowed(account);
		int followingCount = followRepository.countByFollower(account);

		Account loginUser = accountRepository.findById(myAccount.getId()).orElse(null);
		boolean isFollowing = false;
		if (loginUser != null && !loginUser.getId().equals(account.getId())) {
			isFollowing = followRepository.existsByFollowerAndFollowed(loginUser, account);
		}
		model.addAttribute("myAccount", myAccount);
		model.addAttribute("isFollowing", isFollowing);

		model.addAttribute("account", account);
		model.addAttribute("items", items);
		model.addAttribute("reviews", reviews);
		model.addAttribute("avgScore", avgScore != null ? String.format("%.1f", avgScore) : "未評価");
		model.addAttribute("followerCount", followerCount);
		model.addAttribute("followingCount", followingCount);

		return "user/user_detail";
	}

}
