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
import com.example.demo.service.BookmarkService;
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
	BookmarkService bookmarkService;

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
			@RequestParam("price") String priceStr,
			@RequestParam("category_id") Long categoryId,
			@RequestParam("memo") String memo,
			@RequestParam("image_file") MultipartFile imageFile,
			Model model) {

		boolean hasError = false;

		// 商品名バリデーション
		if (itemName == null || itemName.trim().isEmpty()) {
			model.addAttribute("itemNameMessage", "商品名を入力してください");
			hasError = true;
		} else if (itemName.length() > 50) {
			model.addAttribute("itemNameMessage", "商品名は50文字以内で入力してください");
			hasError = true;
		}

		// 価格バリデーション
		Integer price = null;
		try {
			price = Integer.parseInt(priceStr);
			if (price <= 0) {
				model.addAttribute("priceMessage", "価格は1円以上を入力してください");
				hasError = true;
			}
		} catch (NumberFormatException e) {
			model.addAttribute("priceMessage", "価格は数値で入力してください");
			hasError = true;
		}

		// カテゴリバリデーション
		if (categoryId == null || categoryId == 0) {
			model.addAttribute("categoryMessage", "カテゴリを選択してください");
			hasError = true;
		}

		// 画像バリデーション（必須）
		if (imageFile == null || imageFile.isEmpty()) {
			model.addAttribute("imageMessage", "画像を選択してください");
			hasError = true;
		}

		if (hasError) {
			model.addAttribute("categories", categoryRepository.findAll());
			return "item/item_new";
		}

		try {
			// 保存ディレクトリ作成
			String uploadDir = new File("src/main/resources/static/images/items").getAbsolutePath();
			Files.createDirectories(Paths.get(uploadDir));

			// ファイル名取得とチェック
			String originalFilename = imageFile.getOriginalFilename();
			if (originalFilename == null || originalFilename.isBlank()) {
				model.addAttribute("imageMessage", "有効な画像ファイルを選択してください");
				model.addAttribute("categories", categoryRepository.findAll());
				return "item/item_new";
			}

			// 上書き防止（同名ファイルがある場合は連番を付与）
			Path filePath = Paths.get(uploadDir, originalFilename);
			String baseName = originalFilename.contains(".")
					? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
					: originalFilename;
			String extension = originalFilename.contains(".")
					? originalFilename.substring(originalFilename.lastIndexOf('.'))
					: "";

			int count = 1;
			while (Files.exists(filePath)) {
				filePath = Paths.get(uploadDir, baseName + "_" + count + extension);
				count++;
			}

			imageFile.transferTo(filePath.toFile());

			// 保存処理
			Long userId = myAccount.getId();
			itemService.saveNewItem(itemName, filePath.getFileName().toString(), price, memo, userId, categoryId);

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
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));
		model.addAttribute("item", item);

		List<Chat> unreadChats = chatService.getUnreadChatsForCurrentUser();

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

		model.addAttribute("unreadChats", unreadChats);

		// ログインユーザーがこの商品をブックマークしているかどうか
		if (myAccount.getId() != null) {
			Account loginUser = accountRepository.findById(myAccount.getId()).orElse(null);
			if (loginUser != null) {
				boolean isBookmarked = bookmarkService.isBookmarked(loginUser, item);
				model.addAttribute("isBookmarked", isBookmarked);
			}
		}

		return "item/item_detail";
	}

	@GetMapping("/items/{id}/edit")
	public String itemEdit(@PathVariable("id") Long id, Model model) {
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("item", item);
		model.addAttribute("categories", categories);

		// ブックマーク情報の追加
		Account loginUser = accountRepository.findById(myAccount.getId()).orElseThrow();
		boolean isBookmarked = bookmarkRepository.findByUserAndItem(loginUser, item).isPresent();
		model.addAttribute("isBookmarked", isBookmarked);

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

	// 商品編集処理
	@PostMapping("/items/{id}/edit")
	public String updateItem(
			@PathVariable("id") Long itemId,
			@RequestParam("item_name") String itemName,
			@RequestParam("price") Integer price,
			@RequestParam("category_id") Long categoryId,
			@RequestParam("memo") String memo,
			@RequestParam("image_file") MultipartFile imageFile,
			Model model) {

		boolean hasError = false;

		// 商品名チェック
		if (itemName == null || itemName.trim().isEmpty()) {
			model.addAttribute("itemNameMessage", "商品名を入力してください");
			hasError = true;
		} else if (itemName.length() > 50) {
			model.addAttribute("itemNameMessage", "商品名は50文字以内で入力してください");
			hasError = true;
		}

		// 価格チェック
		if (price == null || price <= 0) {
			model.addAttribute("priceMessage", "価格は1円以上を入力してください");
			hasError = true;
		}

		// カテゴリチェック
		if (categoryId == null || categoryId == 0) {
			model.addAttribute("categoryMessage", "カテゴリを選択してください");
			hasError = true;
		}

		Item item = itemRepository.findById(itemId).orElseThrow();

		if (hasError) {
			model.addAttribute("item", item);
			model.addAttribute("categories", categoryRepository.findAll());
			return "item/item_edit";
		}

		try {
			String imageFilename = item.getImage(); // デフォルトで既存の画像
			if (imageFile != null && !imageFile.isEmpty()) {
				String uploadDir = new File("src/main/resources/static/images/items").getAbsolutePath();
				Files.createDirectories(Paths.get(uploadDir));
				imageFilename = imageFile.getOriginalFilename();
				Path filePath = Paths.get(uploadDir, imageFilename);
				imageFile.transferTo(filePath.toFile());
			}

			item.setName(itemName);
			item.setPrice(price);
			item.setMemo(memo);
			item.setImage(imageFilename);
			item.setCategory(categoryRepository.findById(categoryId).orElseThrow());

			itemRepository.save(item);

			return "redirect:/items/" + itemId + "/detail";

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

}
