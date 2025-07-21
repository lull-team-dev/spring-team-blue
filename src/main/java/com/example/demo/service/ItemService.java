package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.entity.Account;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private MyAccount myAccount;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private AccountRepository accountRepository;

	public void loadItemPage(Integer categoryId, String keyword, Model model) {

		// カテゴリー一覧取得
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		List<Item> itemList = new ArrayList<>();

		// ログイン中のユーザーIDを取得
		Long currentUserId = myAccount.getId();

		if (currentUserId != null) {
			// ログイン時 → 自分以外の出品のみ
			if (categoryId == null && (keyword == null || keyword.isBlank())) {
				itemList = itemRepository.findByAccountIdNot(currentUserId);
			} else if (categoryId != null && (keyword == null || keyword.isBlank())) {
				itemList = itemRepository.findByCategoryIdAndAccountIdNot(categoryId, currentUserId);
			} else {
				// キーワード検索結果から自分の出品を除外
				List<Item> keywordMatched = itemRepository.findByNameContaining(keyword);
				itemList = keywordMatched.stream()
						.filter(item -> !item.getAccount().getId().equals(currentUserId))
						.toList();
				if (itemList.isEmpty()) {
					model.addAttribute("message", "お探しの商品は見つかりませんでした");
					itemList = itemRepository.findByAccountIdNot(currentUserId);
				}
			}
		} else {
			// 未ログイン時
			if (categoryId == null && (keyword == null || keyword.isBlank())) {
				itemList = itemRepository.findAll();
			} else if (categoryId != null && (keyword == null || keyword.isBlank())) {
				itemList = itemRepository.findByCategoryId(categoryId);
			} else {
				itemList = itemRepository.findByNameContaining(keyword);
				if (itemList.isEmpty()) {
					model.addAttribute("message", "お探しの商品は見つかりませんでした");
					itemList = itemRepository.findAll();
				}
			}
		}

		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("items", itemList);
	}

	// 商品を保存（新規追加）
	@Transactional
	public void saveNewItem(String name, String image, Integer price, String memo, Long userId, Long categoryId) {
		Item item = new Item();
		item.setName(name);
		item.setImage(image); // ここは "namekuzi.jpg" のようなファイル名だけ
		item.setPrice(price);
		item.setMemo(memo);
		item.setSoldOut(false);

		Account account = accountRepository.findById(userId).orElseThrow();
		Category category = categoryRepository.findById(categoryId).orElseThrow();

		item.setAccount(account);
		item.setCategory(category);

		itemRepository.save(item);
	}

	@Transactional
	public void updateItem(Long id, String name, Integer price, Long categoryId, String memo, String imageFileName) {
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("該当商品が見つかりません"));

		item.setName(name);
		item.setPrice(price);
		item.setMemo(memo);
		item.setCategory(categoryRepository.findById(categoryId).orElse(null));
		if (imageFileName != null && !imageFileName.isBlank()) {
			item.setImage(imageFileName);
		}

		itemRepository.save(item);
	}

	@Transactional
	public void toggleSoldOut(Long itemId, Boolean soldOut) {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new IllegalArgumentException("該当商品が見つかりません"));
		item.setSoldOut(soldOut);
		itemRepository.save(item);
	}

}
