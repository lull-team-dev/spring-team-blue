package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.entity.History;
import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;

@Controller
public class OrderController {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	HttpSession session;
	@Autowired
	MyAccount myAccount;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	HistoryRepository historyRepository;

	// 商品購入ページを表示（売り切れならリダイレクト）
	@GetMapping("/order/{itemId}")
	public String showOrderForm(@PathVariable("itemId") Long itemId, Model model) {
		Item item = itemRepository.findById(itemId).orElseThrow();

		if (item.isSoldOut()) {
			model.addAttribute("errorMessage", "この商品は既に売り切れました。");
			return "order/order_error"; // エラーページへ
		}

		Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
		model.addAttribute("item", item);
		model.addAttribute("account", account);
		return "order/order_form";
	}

	// 購入確認画面を表示
	@PostMapping("/order/confirm")
	public String showDoOrder(
			@RequestParam("postal_code") String postalCode,
			@RequestParam("address") String address,
			@RequestParam("payment_method") String paymentMethod,
			@RequestParam("item_id") Long itemId,
			Model model) {

		Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();

		// 売り切れチェック（念のため）
		if (item.isSoldOut()) {
			model.addAttribute("errorMessage", "この商品は既に売り切れました。");
			return "order/order_error";
		}

		Order order = new Order();
		order.setAccount(account);
		order.setItem(item);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalPrice(item.getPrice());
		order.setStatus((short) 0); // 仮状態
		order.setPostalCode(postalCode);
		order.setDeliveryAddress(address);
		order.setDeliveryTel(account.getTel());
		order.setPaymentMethod(paymentMethod);

		model.addAttribute("account", account);
		model.addAttribute("order", order);
		model.addAttribute("item", item);

		return "order/order_confirm";
	}

	// 購入完了ページ（売り切れなら防止）
	@PostMapping("/order/confirm/complete")
	public String orderComplete(@RequestParam("address") String address,
			@RequestParam("payment_method") String paymentMethod,
			@RequestParam("itemId") Long itemId,
			@RequestParam("postalCode") String postalCode,
			Model model) {

		Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();

		if (item.isSoldOut() == true) {
			model.addAttribute("errorMessage", "この商品は既に売り切れました。");
			return "order/order_error";
		}

		History history = new History(account, item, LocalDateTime.now(), item.getPrice());
		historyRepository.save(history);
		item.setSoldOut(true);
		itemRepository.save(item);

		Order order = new Order();
		order.setAccount(account);
		order.setItem(item);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalPrice(item.getPrice());
		order.setStatus((short) 1); // 完了
		order.setPostalCode(postalCode);
		order.setDeliveryAddress(address);
		order.setDeliveryTel(account.getTel());
		order.setPaymentMethod(paymentMethod);

		orderRepository.save(order);

		model.addAttribute("order", order);
		model.addAttribute("item", item);

		return "order/order_result";
	}

	// 購入履歴ページ
	@GetMapping("/mypage/orders")
	public String showPurchaseHistory(Model model) {
		Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
		List<Order> orders = orderRepository.findByAccountId(account.getId());
		model.addAttribute("orders", orders);
		return "mypage/purchase_history";
	}
}
