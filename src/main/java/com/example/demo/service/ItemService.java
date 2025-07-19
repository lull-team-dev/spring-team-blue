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
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private AccountRepository accountRepository;

	public void loadItemPage(Integer categoryId, String keyword, Model model) {

		// カテゴリー一覧取得
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		// 商品一覧取得（絞り込みあり）
		List<Item> itemList = new ArrayList<>();

		if (categoryId == null && keyword == null) {
			itemList = itemRepository.findAll();
		} else {
			if (categoryId != null) {
				itemList = itemRepository.findByCategoryId(categoryId);
			}
			if (keyword != null) {
				itemList = itemRepository.findByNameContaining(keyword);
				if (itemList.isEmpty()) {
					model.addAttribute("message", "お探しの商品は見つかりませんでした");
					itemList = itemRepository.findAll();
				}
			}
		}
		// カテゴリーIDによってスタイルを変更したいため追加
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
	public void toggleSoldOut(Long itemId, Boolean soldOut) {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new IllegalArgumentException("該当商品が見つかりません"));
		item.setSoldOut(soldOut);
		itemRepository.save(item);
	}

}
