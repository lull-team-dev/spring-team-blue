package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MypageController {

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
