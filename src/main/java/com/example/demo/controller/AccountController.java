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
	public String login(
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			Model model) {

		boolean hasError = false;

		// 入力保持
		model.addAttribute("oldEmail", email);

		// バリデーション
		if (email.isEmpty()) {
			model.addAttribute("emailMessage", "メールアドレスを入力してください");
			hasError = true;
		} else if (!email.matches("^[\\w+\\-.]+@[a-z\\d\\-.]+\\.[a-z]{2,6}$")) {
			model.addAttribute("emailMessage", "メールアドレスの形式が正しくありません");
			hasError = true;
		}

		if (password.isEmpty()) {
			model.addAttribute("passwordMessage", "パスワードを入力してください");
			hasError = true;
		}

		if (hasError) {
			return "account/login";
		}

		Account account = accountRepository.findByEmailAndPassword(email, password);
		if (account == null) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが一致しません");
			return "account/login";
		}

		myAccount.setId(account.getId());
		myAccount.setName(account.getName());
		myAccount.setNickname(account.getNickname());
		myAccount.setIcon(account.getIcon());
		session.setAttribute("account", myAccount);

		// リダイレクト先が指定されている場合はそこへ
		String redirectPath = (String) session.getAttribute("redirectAfterLogin");
		if (redirectPath != null && redirectPath.startsWith("/")) {
			session.removeAttribute("redirectAfterLogin");
			return "redirect:" + redirectPath;
		}

		return "redirect:/items";
	}

	// 登録フォーム
	@GetMapping("/register")
	public String showRegisterForm() {
		return "account/register";
	}

	// 登録 → 確認画面へリダイレクト
	@PostMapping("/register")
	public String register(
			@RequestParam(name = "last_name", defaultValue = "") String lastName,
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

		// ▼ 入力保持（テンプレート側で th:value に反映させる）
		model.addAttribute("oldLastName", lastName);
		model.addAttribute("oldFirstName", firstName);
		model.addAttribute("oldNickName", nickname);
		model.addAttribute("oldEmail", email);
		model.addAttribute("oldZip1", zip1);
		model.addAttribute("oldZip2", zip2);
		model.addAttribute("oldPrefecture", prefecture);
		model.addAttribute("oldCity", city);
		model.addAttribute("oldTown", town);
		model.addAttribute("oldBuilding", building);
		model.addAttribute("oldTel", tel);

		// ▼ バリデーション
		if (lastName.isEmpty() || firstName.isEmpty()) {
			model.addAttribute("nameMessage", "姓名は必ず入力してください。");
			hasError = true;
		}
		if (nickname.isEmpty()) {
			model.addAttribute("nickNameMessage", "ニックネームを入力してください");
			hasError = true;
		}
		if (email.isEmpty()) {
			model.addAttribute("emailMessage", "メールアドレスを入力してください");
			hasError = true;
		} else if (!email.matches("^[\\w+\\-.]+@[a-z\\d\\-.]+\\.[a-z]{2,6}$")) {
			model.addAttribute("emailMessage", "メールアドレスの形式が正しくありません");
			hasError = true;
		}
		if (password.isEmpty() || confirmPass.isEmpty()) {
			model.addAttribute("passMessage", "パスワードを入力してください");
			hasError = true;
		} else if (!password.equals(confirmPass)) {
			model.addAttribute("passwordMismatchMessage", "パスワードが一致しませんでした。");
			hasError = true;
		} else if (password.length() < 6) {
			model.addAttribute("passMessage", "パスワードは6文字以上で入力してください");
			hasError = true;
		}
		if (zip1.isEmpty() || zip2.isEmpty()) {
			model.addAttribute("zipMessage", "郵便番号をすべて入力してください");
			hasError = true;
		} else if (!zip1.matches("\\d{3}") || !zip2.matches("\\d{4}")) {
			model.addAttribute("zipMessage", "郵便番号は「123-4567」の形式で入力してください");
			hasError = true;
		}
		if (prefecture.isEmpty() || city.isEmpty() || town.isEmpty()) {
			model.addAttribute("addressMessage", "住所（都道府県・市区町村・町域）をすべて入力してください");
			hasError = true;
		}
		if (!tel.matches("\\d{10,11}") && !tel.matches("\\d{2,4}-\\d{2,4}-\\d{3,4}")) {
			model.addAttribute("telMessage", "電話番号の形式が正しくありません");
			hasError = true;
		}

		// ▼ エラーがある場合
		if (hasError) {
			return "account/register";
		}

		// ▼ メール重複チェック
		Account mailCheck = accountRepository.findByEmail(email);
		if (mailCheck != null) {
			model.addAttribute("emailMessage", "入力いただいたメールアドレスはすでに登録済みです。");
			return "account/register";
		}

		// ▼ 登録データ整形と保存
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
		return "account/register_complete";
	}

}
