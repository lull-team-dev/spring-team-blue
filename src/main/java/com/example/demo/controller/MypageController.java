package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.entity.Item;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ItemRepository;

@Controller
public class MypageController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ItemRepository itemRepository;

	// マイページ
	@GetMapping("/mypage")
	public String showMypage() {
		//マイページの表示の仕方をカテゴリー選択ごとに変える（お気に入りが追加機能のため、一旦型だけ作って明日やる。）
		return "mypage/mypage";
	}

	// マイページ編集
	@GetMapping("/mypage/update")
	public String showMypageEditForm() {
		return "mypage/mypage_edit";
	}

	// 編集完了 →　マイページへ
	@PostMapping("/mypage/update")
	public String updateMypage(@RequestParam(value = "id", defaultValue = "") Integer id,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "password", defaultValue = "") String password,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "profile", defaultValue = "") String profile,
			@RequestParam(value = "address", defaultValue = "") String address,
			@RequestParam(value = "tel", defaultValue = "") String tel) {

		Account UpdateAccount = accountRepository.findById(id).get();

		UpdateAccount.setName(name);
		UpdateAccount.setEmail(email);
		UpdateAccount.setPassword(password);
		UpdateAccount.setProfile(profile);
		UpdateAccount.setAddress(address);
		UpdateAccount.setTel(tel);

		accountRepository.save(UpdateAccount);

		return "redirect:/mypage";
	}

	// ユーザー詳細
	@GetMapping("/user/{id}/detail")
	public String showUserDetail(@PathVariable("id") Integer id, Model model) {
		Account account = accountRepository.findById(id).orElse(null);
		//ユーザーの商品一覧を取得
		List<Item> items = itemRepository.findByAccountId(id);
		model.addAttribute("account", account);
		model.addAttribute("items", items);
		return "user/user_detail";
	}

}
