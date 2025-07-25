package com.example.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.Category;
import com.example.demo.entity.Chat;
import com.example.demo.entity.Item;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.ChatService;
import com.example.demo.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	BookmarkRepository bookmarkRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	MyAccount myAccount;

	@Autowired
	ItemService itemService;

	@Autowired
	ChatService chatService;

	// 商品登録フォーム
	@GetMapping("items/new")
	public String showItemForm() {
		return "item/item_new";
	}

	// 商品登録完了 → 完了画面へリダイレクト
	@PostMapping("items/submit")
	public String submitItem(
			@RequestParam("item_name") String itemName,
			@RequestParam("price") Integer price,
			@RequestParam("category_id") Long categoryId,
			@RequestParam("memo") String memo,
			@RequestParam("image_file") MultipartFile imageFile) {

		try {
			// ✅ 保存先の絶対パス（static/images/items）
			String uploadDir = new File("src/main/resources/static/images/items").getAbsolutePath();
			Files.createDirectories(Paths.get(uploadDir)); // フォルダが無ければ作成

			// ✅ ファイル名（重複防止にUUIDつけてもよい）
			String originalFilename = imageFile.getOriginalFilename();
			Path filePath = Paths.get(uploadDir, originalFilename);

			// ✅ 画像ファイル保存
			imageFile.transferTo(filePath.toFile());

			// 仮ユーザーID
			Long userId = (long) 1;

			// ✅ 画像ファイル名を保存（パスは不要）
			itemService.saveNewItem(itemName, originalFilename, price, memo, userId, categoryId);

			// ✅ 商品詳細に遷移するように変更も可能（任意）
			return "redirect:/submit/complete";

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// 商品登録完了画面
	@GetMapping("/submit/complete")
	public String showSubmitComplete() {
		return "item/item_submit_complete";
	}

	// 商品一覧

	@GetMapping({ "/items", "/" })
	public String index(
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "keyword", required = false) String keyword,
			Model model) {
		List<Chat> Chats = chatService.getUnreadChatsForCurrentUser();
		model.addAttribute("unreadChats", Chats);
		itemService.loadItemPage(categoryId, keyword, model);

		if (myAccount.getId() != null) {
			Account user = accountRepository.findById(myAccount.getId()).orElse(null);
			if (user != null) {
				List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
				List<Long> bookmarkedItemIds = bookmarks.stream()
						.map(b -> b.getItem().getId())
						.collect(Collectors.toList());
				model.addAttribute("bookmarkedItemIds", bookmarkedItemIds);
			}
		}

		return "item/item_list";
	}

	// 商品詳細
	@GetMapping("/items/{id}/detail")
	public String showItemDetail(@PathVariable("id") Long id, Model model) {
		Item item = itemRepository.findById(id).orElse(null); // orElseThrowでもOK
		List<Chat> unreadChats = chatService.getUnreadChatsForCurrentUser();
		model.addAttribute("unreadChats", unreadChats);
		model.addAttribute("item", item);
		return "item/item_detail";
	}

	@GetMapping("/items/{id}/edit")
	public String itemEdit(@PathVariable("id") Long id, Model model) {
		Item item = itemRepository.findById(id).get();
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("item", item);
		model.addAttribute("categories", categories);
		return "item/item_edit";
	}

	@PostMapping("/item/toggle-soldout")
	@ResponseBody
	public ResponseEntity<String> toggleSoldOut(@RequestParam Long itemId,
			@RequestParam Boolean soldOut) {
		try {
			itemService.toggleSoldOut(itemId, soldOut);
			return ResponseEntity.ok("更新成功");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更新失敗: " + e.getMessage());
		}
	}

}
