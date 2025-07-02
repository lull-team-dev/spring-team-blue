package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;

@Controller
public class AccountController {

	@Autowired
	MyAccount myAccount;

	@Autowired
	AccountRepository accountRepository;

	// ログイン画面
	@GetMapping("/login")
	public String index() {
		return "account/login";
	}

	// ログイン処理
	@PostMapping("/login")
	public String login(@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			Model model) {

		if (email == null || email.length() == 0) {
			model.addAttribute("message", "メールアドレスを入力してください");
			return "account/login";

		} else if (password == null || password.length() == 0) {
			model.addAttribute("message", "パスワードを入力してください");
			return "account/login";
		}

		Account account = accountRepository.findByEmailAndPassword(email, password);
		if (account == null) {
			model.addAttribute("message", "メールアドレスまたはパスワードが一致しません");
			return "account/login";
		}

		myAccount.setId(account.getId());
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
