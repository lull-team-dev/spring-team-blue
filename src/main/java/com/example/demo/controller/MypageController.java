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
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.Chat;
import com.example.demo.entity.History;
import com.example.demo.entity.Item;
import com.example.demo.entity.Review;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ChatService;

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
	@Autowired
	BookmarkRepository bookmarkRepository;
	@Autowired
	ChatRepository chatRepository;
	@Autowired
	ChatService chatService;

	// マイページ
	@GetMapping("")
	public String showMypage(Model model) {
		Account loginUser = accountRepository.findById(myAccount.getId()).orElse(null);
		if (loginUser != null) {
			int followerCount = followRepository.countByFollowed(loginUser);
			int followingCount = followRepository.countByFollower(loginUser);

			// ★ 追加：レビュー平均スコアの計算
			List<Review> reviews = reviewRepository.findByReviewee(loginUser);
			double avgScore = 0.0;
			if (!reviews.isEmpty()) {
				double total = reviews.stream().mapToInt(r -> r.getScore()).sum();
				avgScore = total / reviews.size();
			}

			List<Chat> Chats = chatService.getUnreadChatsForCurrentUser();
			model.addAttribute("unreadChats", Chats);
			model.addAttribute("avgScore", avgScore);

			model.addAttribute("followerCount", followerCount);
			model.addAttribute("followingCount", followingCount);
			model.addAttribute("account", loginUser);
		}
		return "mypage/mypage";
	}

	@GetMapping("/{id}")
	public String showMoreMypage(@PathVariable(name = "id") Long id, Model model) {
		Account account = accountRepository.findById(myAccount.getId()).get();

		if (id == 1) {
			List<History> historys = historyRepository.findByAccount(account);
			model.addAttribute("historys", historys);
		} else if (id == 2) {
			List<Item> itemSelling = itemRepository.findByAccount(account);
			model.addAttribute("itemSelling", itemSelling);
		} else if (id == 3) {

			List<Review> myReview = reviewRepository.findByReviewee(account);
			model.addAttribute("myReview", myReview);

		} else if (id == 4) {
			Account user = accountRepository.findById(myAccount.getId()).orElseThrow();
			List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
			model.addAttribute("bookmarks", bookmarks);
		}

		if (account != null) {
			int followerCount = followRepository.countByFollowed(account);
			int followingCount = followRepository.countByFollower(account);

			// ★ 追加：レビュー平均スコアの計算
			List<Review> reviews = reviewRepository.findByReviewee(account);
			double avgScore = 0.0;
			if (!reviews.isEmpty()) {
				double total = reviews.stream().mapToInt(r -> r.getScore()).sum();
				avgScore = total / reviews.size();
			}

			List<Chat> unreadChats = chatService.getUnreadChatsForCurrentUser();
			model.addAttribute("unreadChats", unreadChats);
			model.addAttribute("avgScore", avgScore);
			model.addAttribute("currentTab", id);

			model.addAttribute("followerCount", followerCount);
			model.addAttribute("followingCount", followingCount);
			model.addAttribute("account", account);
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
			@RequestParam(value = "tel", defaultValue = "") String tel,
			@RequestParam(name = "zip1", defaultValue = "") String zip1,
			@RequestParam(name = "zip2", defaultValue = "") String zip2,
			@RequestParam(name = "prefecture", defaultValue = "") String prefecture,
			@RequestParam(name = "city", defaultValue = "") String city,
			@RequestParam(name = "town", defaultValue = "") String town,
			@RequestParam(name = "building", defaultValue = "") String building) {

		Account UpdateAccount = accountRepository.findById(id).get();

		UpdateAccount.setName(name);
		UpdateAccount.setPassword(password);
		UpdateAccount.setEmail(email);
		UpdateAccount.setProfile(profile);
		UpdateAccount.setPrefecture(prefecture);
		UpdateAccount.setCity(city);
		UpdateAccount.setTown(town);
		UpdateAccount.setBuilding(building);
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

		List<Item> items = itemRepository.findByAccount(account);
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
