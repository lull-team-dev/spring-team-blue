package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

	// ログイン画面
	@GetMapping("/login")
	public String index() {
		return "account/login";
	}

	// ログイン処理
	@PostMapping("/login")
	public String login() {
		return "redirect:item/item_list";
	}

	// 登録フォーム
	@GetMapping("/register")
	public String showRegisterForm() {
		return "account/register";
	}

	// 登録 → 確認画面へリダイレクト
	@PostMapping("/register")
	public String register() {
		return "redirect:/register/confirm";
	}

	// 確認画面
	@GetMapping("/register/confirm")
	public String showConfirmation() {
		return "account/register_confirm";
	}

	// 登録完了 → ログイン画面へ
	@PostMapping("/register/complete")
	public String registerComplete() {
		return "redirect:/login";
	}

	// マイページ
	@GetMapping("/mypage")
	public String showMypage() {
		return "mypage/mypage";
	}

	// マイページ編集
	@GetMapping("/mypage/update")
	public String showMypageEditForm() {
		return "mypage/mypage_edit";
	}

	// 編集完了 →　マイページへ
	@PostMapping("/mypage/update")
	public String updateMypage() {
		return "redirect:/mypage";
	}

	// ユーザー詳細
	@GetMapping("/user/{id}/detail")
	public String showUserDetail(@PathVariable("id") Long id) {
		return "user/user_detail";
	}
}
