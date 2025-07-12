package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Account;
import com.example.demo.entity.Follow;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FollowRepository;

@Controller
@RequestMapping("/mypage/user")
public class FollowController {

	@Autowired
	private MyAccount myAccount;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FollowRepository followRepository;

	// フォロー実行
	@PostMapping("/{followedId}")
	public String follow(@PathVariable("followedId") Long followedId) {
		Account follower = accountRepository.findById(myAccount.getId()).orElse(null);
		Account followed = accountRepository.findById(followedId).orElse(null);

		if (follower == null || followed == null)
			return "error";

		if (!followRepository.existsByFollowerAndFollowed(follower, followed)) {
			Follow follow = new Follow();
			follow.setFollower(follower);
			follow.setFollowed(followed);
			followRepository.save(follow);
		}

		return "redirect:/mypage/user/" + followedId + "/detail";
	}

	// フォロー解除
	@PostMapping("/{followedId}/unfollow")
	public String unfollow(@PathVariable("followedId") Long followedId) {
		Account follower = accountRepository.findById(myAccount.getId()).orElse(null);
		Account followed = accountRepository.findById(followedId).orElse(null);

		if (follower == null || followed == null)
			return "error";

		Follow follow = followRepository.findByFollowerAndFollowed(follower, followed);
		if (follow != null) {
			followRepository.delete(follow);
		}

		return "redirect:/mypage/user/" + followedId + "/detail";
	}

	// フォロワー一覧表示
	@GetMapping("/{id}/followers")
	public String showFollowers(@PathVariable("id") Long id, Model model) {
		Account account = accountRepository.findById(id).orElse(null);
		if (account == null)
			return "error";

		List<Follow> followers = followRepository.findByFollowed(account);
		model.addAttribute("account", account);
		model.addAttribute("followers", followers);
		return "user/followers";
	}

	// フォロー一覧表示
	@GetMapping("/{id}/following")
	public String showFollowing(@PathVariable("id") Long id, Model model) {
		Account account = accountRepository.findById(id).orElse(null);
		if (account == null)
			return "error";

		List<Follow> following = followRepository.findByFollower(account);
		model.addAttribute("account", account);
		model.addAttribute("following", following);
		return "user/following";
	}
}
