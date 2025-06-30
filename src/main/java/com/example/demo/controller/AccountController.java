package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
		return "redirect:/items";
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

}
