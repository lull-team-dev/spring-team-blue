package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Account;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	MyAccount myAccount;

	@Autowired
	AccountRepository accountRepository;

	// ログイン画面
	@GetMapping({ "/login", "/logout" })
	public String index() {
		session.removeAttribute("account"); // ← セッション全体を消さず、ログイン情報だけクリア
		return "account/login";
	}

	// ログイン処理
	@PostMapping("/login")
	public String login(@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			Model model) {

		if (email.isEmpty()) {
			model.addAttribute("message", "メールアドレスを入力してください");
			return "account/login";
		}

		if (password.isEmpty()) {
			model.addAttribute("message", "パスワードを入力してください");
			return "account/login";
		}

		Account account = accountRepository.findByEmailAndPassword(email, password);
		if (account == null) {
			model.addAttribute("message", "メールアドレスまたはパスワードが一致しません");
			return "account/login";
		}

		myAccount.setId(account.getId());
		myAccount.setName(account.getName());
		myAccount.setNickname(account.getNickname());
		session.setAttribute("account", myAccount);

		// 🔽 セッションに保存されたリダイレクト先があるならそこへ
		String redirectPath = (String) session.getAttribute("redirectAfterLogin");
		if (redirectPath != null && redirectPath.startsWith("/")) {
			session.removeAttribute("redirectAfterLogin");
			return "redirect:" + redirectPath;
		}

		// デフォルトはトップや商品一覧などへ
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
			@RequestParam(name = "nickName", defaultValue = "") String nickname,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirmPass", defaultValue = "") String confirmPass,
			@RequestParam(name = "zip1", defaultValue = "") String zip1,
			@RequestParam(name = "zip2", defaultValue = "") String zip2,
			@RequestParam(name = "prefecture", defaultValue = "") String prefecture,
			@RequestParam(name = "city", defaultValue = "") String city,
			@RequestParam(name = "town", defaultValue = "") String town,
			@RequestParam(name = "building", defaultValue = "") String building,
			@RequestParam(name = "tel", defaultValue = "") String tel,

			Model model,
			RedirectAttributes redirectAttributes) {

		boolean hasError = false;
		Account addAccount = new Account();

		// 姓名入力チェック
		if (lastName.isEmpty() || firstName.isEmpty()) {
			model.addAttribute("nameMessage", "姓名は必ず入力してください。");
			hasError = true;
		}
		// ニックネーム入力チェック
		if (nickname.isEmpty()) {
			model.addAttribute("nickNameMessage", "ニックネームを入力してください");
			hasError = true;
		}
		// メール入力チェック
		if (email.isEmpty()) {
			model.addAttribute("emailMessage", "メールアドレスを入力してください");
			hasError = true;
		}
		// パスワード入力チェック
		if (password.isEmpty() || confirmPass.isEmpty()) {
			model.addAttribute("passMessage", "パスワードを入力してください");
		} else if (!password.equals(confirmPass)) {
			model.addAttribute("passwordMismatchMessage", "パスワードが一致しませんでした。");
			hasError = true;
		}

		// 郵便番号チェック
		if (zip1.isEmpty() || zip2.isEmpty()) {
			model.addAttribute("zipMessage", "郵便番号をすべて入力してください");
			hasError = true;
		} else if (!zip1.matches("\\d{3}") || !zip2.matches("\\d{4}")) {
			model.addAttribute("zipMessage", "郵便番号は「123-4567」の形式で入力してください");
			hasError = true;
		}

		// 住所チェック
		if (prefecture.isEmpty() || city.isEmpty() || town.isEmpty()) {
			model.addAttribute("addressMessage", "住所（都道府県・市区町村・町域）をすべて入力してください");
			hasError = true;
		}

		if (hasError) {
			return "account/register";
		}

		// メールの登録有無
		Account mailCheck = accountRepository.findByEmail(email);

		if (mailCheck != null) {
			model.addAttribute("emailMessage", "入力いただいたメールアドレスはすでに登録済みです。");
			return "account/register";
		}

		// 登録データセット
		String fullName = lastName + firstName;
		String fullZip = zip1 + "-" + zip2;

		addAccount.setName(fullName);
		addAccount.setNickname(nickname);
		addAccount.setEmail(email);
		addAccount.setPassword(password);
		addAccount.setZip(fullZip);
		addAccount.setPrefecture(prefecture);
		addAccount.setCity(city);
		addAccount.setTown(town);
		addAccount.setBuilding(building);
		addAccount.setTel(tel);

		redirectAttributes.addFlashAttribute("account", addAccount);
		return "redirect:/account/register/confirm";
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
		return "redirect:/account/login";
	}

}
