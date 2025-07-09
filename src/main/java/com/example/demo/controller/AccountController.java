package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Account;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;

@Controller
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	MyAccount myAccount;

	@Autowired
	AccountRepository accountRepository;

	// ログイン画面
	@GetMapping("/login")
	public String index() {

		session.invalidate();
		return "account/login";
	}

	// ログイン処理
	@PostMapping("/login")
	public String login(@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			Model model) {

		if (email.isEmpty() || email.length() == 0) {

			model.addAttribute("emailMessage", "メールアドレスを入力してください");

			if (password.isEmpty() || password.length() == 0) {
				model.addAttribute("passMessage", "パスワードを入力してください");
			}
			return "account/login";
		}

		Account account = accountRepository.findByEmailAndPassword(email, password);
		if (account == null) {
			model.addAttribute("message", "メールアドレスまたはパスワードが一致しません");
			return "account/login";
		}
		myAccount.setId(account.getId());
		myAccount.setName(account.getName());

		session.setAttribute("account", myAccount);
		return "redirect:/items";
	}

	// 登録フォーム
	@GetMapping("/register")
	public String showRegisterForm() {
		return "account/register";
	}

	// 登録 → 確認画面へリダイレクト
	@PostMapping("/register")
	public String register(@RequestParam(name = "last_name", defaultValue = "") String lastName,
			@RequestParam(name = "first_name", defaultValue = "") String firstName,
			@RequestParam(name = "nickName", defaultValue = "") String nickName,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirmPass", defaultValue = "") String confirmPass,
			@RequestParam(name = "address", defaultValue = "") String address,
			@RequestParam(name = "tel", defaultValue = "") String tel,
			Model model,
			RedirectAttributes redirectAttributes) {

		Account addAccount = new Account();
		String fullName;

		if (lastName.isEmpty() || firstName.isEmpty() || nickName.isEmpty() || email.isEmpty()
				|| password.isEmpty() || confirmPass.isEmpty()) {
			//姓名入力チェック
			if (lastName.isEmpty() || firstName.isEmpty()) {
				model.addAttribute("nameMessage", "姓名は必ず入力してください。");
			}
			//ニックネーム入力チェック
			if (nickName.isEmpty()) {
				model.addAttribute("nickNameMessage", "ニックネームを入力してください");
			}
			//メール入力チェック
			if (email.isEmpty()) {
				model.addAttribute("emailMessage", "メールアドレスの入力が必要です。");
			}
			//パスワード入力チェック
			if (password.isEmpty() || confirmPass.isEmpty()) {
				model.addAttribute("passMessage", "パスワードを入力してください");
			}
			return "account/register";
		} else {

			fullName = lastName + firstName;
			addAccount.setName(fullName);

			addAccount.setNickName(nickName);

			//メールの登録有無
			Account mailCheck = accountRepository.findByEmail(email);

			if (mailCheck != null) {
				model.addAttribute("emailMessage", "入力いただいたメールアドレスはすでに登録済みです。");
				return "account/register";
			}

			if (!password.equals(confirmPass)) {
				model.addAttribute("passMessage", "パスワードが一致しませんでした。");
				return "account/register";
			}

			addAccount.setEmail(email);
			addAccount.setPassword(password);
			addAccount.setAddress(address);
			addAccount.setTel(tel);

			redirectAttributes.addFlashAttribute("account", addAccount);
			return "redirect:/register/confirm";
		}
	}

	// 確認画面
	@GetMapping("/register/confirm")
	public String showConfirmation(@ModelAttribute("account") Account account) {
		return "account/register_confirm";
	}

	// 登録完了 → ログイン画面へ
	@PostMapping("/register/complete")
	public String registerComplete(@ModelAttribute(name = "account") Account account,
			Model model) {
		accountRepository.save(account);
		return "redirect:/login";
	}

}
