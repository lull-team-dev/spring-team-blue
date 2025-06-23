package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ItemService itemService;

	// 商品登録フォーム
	@GetMapping("items/new")
	public String showItemForm() {
		return "item/item_new";
	}

	// 商品登録完了 → 完了画面へリダイレクト
	@PostMapping("items/submit")
	public String submitItem() {
		return "redirect:/mypage";
	}

	// 商品登録完了画面
	@GetMapping("/submit/complete")
	public String showSubmitComplete() {
		return "item/item_submit_complete";
	}

	// 商品一覧
	@GetMapping({ "item/item_list", "/" })
	public String index(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model) {
		itemService.loadItemPage(categoryId, model);
		return "item/item_list";
	}

	// 商品詳細
	@GetMapping("items/{id}/detail")
	public String showItemDetail() {
		return "item/item_detail";
	}

}