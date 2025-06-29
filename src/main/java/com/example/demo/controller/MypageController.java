package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MypageController {

	@Autowired
	AccountRepository accountRepository;

	// マイページ
	@GetMapping("/mypage")
	public String showMypage(@RequestParam(value = "id", defaultValue = "") Integer id) {
	//マイページの表示の仕方をカテゴリー選択ごとに変える（お気に入りが追加機能のため、一旦型だけ作って明日やる。）
		if(id == 1){
		}else if(id == 2){
		}else if(id == 3){
		}else if(id == 4){
		}else{

		}

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


		Account UpdateAccount = accountRepository.findById(id);

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
	public String showUserDetail(@PathVariable("id") Integer id) {
		return "user/user_detail";
	}

}
