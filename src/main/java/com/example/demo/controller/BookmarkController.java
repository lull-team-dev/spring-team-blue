package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Account;
import com.example.demo.entity.Item;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.BookmarkService;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	HttpSession session;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private MyAccount myAccount;

	//  // ブックマーク一覧画面表示 ←　これはマイページのshowMoreMypage()メソッドで処理してページでそのまま表示させるので不要
	//  @GetMapping("")
	//  public String showBookmarks(Model model) {
	//    Account user = accountRepository.findById(myAccount.getId()).orElseThrow();
	//    List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
	//    model.addAttribute("bookmarks", bookmarks);
	//    return "bookmark/bookmark_list";
	//  }

	@PostMapping("/toggle-ajax")
	@ResponseBody // JSにレスポンスとして直接返す
	// ♥ボタンでブックマークを登録・削除する
	public String toggleBookmarkAjax(@RequestParam("itemId") Long itemId) {

		// 未ログインで♥を押されたら無効に
		if (myAccount.getId() == null) {
			return "unauthenticated";
		}
		// ログイン中のユーザー情報を取得
		Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
		// 対象商品をDBから取得
		Item item = itemRepository.findById(itemId).orElseThrow();

		boolean isNowBookmarked = bookmarkService.toggleBookmark(account, item);
		// 登録した場合 → "bookmarked"、削除した場合 → "unbookmarked"

		return isNowBookmarked ? "bookmarked" : "unbookmarked";
	}

}