package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

	// 商品購入ページを表示
	@GetMapping("/order")
	public String showConfirm() {
		return "order/order_form";
	}

	// 購入確認画面を表示
	@PostMapping("/order/confirm")
	public String showDoOrder() {
		return "order/order_confirm";
	}

	// 購入完了ページを表示
	@PostMapping("/order/confirm/complete")
	public String orderComplete() {
		return "order/order_result";
	}

}