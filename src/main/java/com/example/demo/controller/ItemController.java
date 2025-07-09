package com.example.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	private ItemService itemService;

	@Autowired
	ItemRepository itemRepository;

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
			@RequestParam("category_id") Integer categoryId,
			@RequestParam("memo") String memo,
			@RequestParam("image_file") MultipartFile imageFile) {

		try {
			String uploadDir = new File("src/main/resources/static/images/items").getAbsolutePath();
			Files.createDirectories(Paths.get(uploadDir));

			String originalFilename = imageFile.getOriginalFilename();
			Path filePath = Paths.get(uploadDir, originalFilename);
			imageFile.transferTo(filePath.toFile());

			Long userId = (long) 1;
			itemService.saveNewItem(itemName, originalFilename, price, memo, userId, categoryId);

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
		itemService.loadItemPage(categoryId, keyword, model);
		return "item/item_list";
	}

	// 商品詳細
	@GetMapping("/items/{id}/detail")
	public String showItemDetail(@PathVariable("id") Integer id, Model model) {
		Item item = itemRepository.findById(id).orElse(null); // orElseThrowでもOK
		model.addAttribute("item", item);
		return "item/item_detail";
	}

}
