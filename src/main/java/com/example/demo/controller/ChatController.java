package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ChatMessage;
import com.example.demo.entity.Account;
import com.example.demo.entity.Chat;
import com.example.demo.entity.Item;
import com.example.demo.entity.Message;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.MessageRepository;

@Controller
public class ChatController {

	@Autowired
	MyAccount myAccount;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	ChatRepository chatRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	MessageRepository messageRepository;

	//ロガー
	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	@MessageMapping("/message")
	@SendTo("/receive/message")
	public ChatMessage chat(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
		//		log.info("handling send message:" + message.getStatement());
		//		return new ChatMessage(HtmlUtils.htmlEscape(message.getName()),
		//				HtmlUtils.htmlEscape(message.getStatement()));
		try {
			System.out.println("handling send message: " + message);
			System.out.println("name: " + message.getName());
			System.out.println("myAccountId: " + message.getName());
			System.out.println("statement: " + message.getStatement());
			System.out.println("chatId: " + message.getChatId());
			System.out.println("itemId: " + message.getItemId());

			Chat chatRoom = chatRepository.findById(message.getChatId()).get();
			Account account = accountRepository.findById(message.getMyAccountId()).get();
			Message messageAdd = new Message(chatRoom, account, message.getStatement(), LocalDateTime.now());

			messageRepository.save(messageAdd);
			// 2. メッセージをそのまま返す
			return message;
		} catch (Exception e) {
			e.printStackTrace(); // ← コンソールにエラーの詳細を出す
			throw e; // ← 必要ならここで新しい例外投げてもOK
		}

		//		System.out.println("handling send message: " + message.getStatement());
		//		return message;
	}

	@PostMapping("/chat")
	public String chatStart(
			@RequestParam(name = "itemId", defaultValue = "") Long itemId,
			@RequestParam(name = "ownerId", defaultValue = "") Long ownerId,
			Model model) {
		Item item = itemRepository.findById(itemId).get();
		Account owner = accountRepository.findById(ownerId).get();
		Account client = accountRepository.findById(myAccount.getId()).get();
		Chat newChatRoom = new Chat(item, client, owner);

		chatRepository.save(newChatRoom);
		Chat chatRoom = chatRepository.findByItemAndClientAndOwner(item, client, owner).get(0);

		return "redirect:/chat?chatId=" + chatRoom.getId();
	}

	@GetMapping("/chat")
	public String showChatPage(
			@RequestParam(name = "chatId") Long chatId,
			Model model) {
		Chat chatRoom = chatRepository.findById(chatId).orElseThrow();
		model.addAttribute("chat", chatRoom);
		model.addAttribute("item", chatRoom.getItem());
		List<Message> messageLogs = messageRepository.findByChat(chatRoom);

		model.addAttribute("messageLogs", messageLogs);
		return "chats/chatMessage";
	}

	//	@MessageMapping("/message")
	//	@SendTo("/receive/message")
	//	public ChatMessage chat(@Payload String rawJson) throws Exception {
	//		System.out.println("受け取った生データ: " + rawJson);
	//
	//		// ここで自前でデシリアライズしてもいい
	//		ObjectMapper mapper = new ObjectMapper();
	//		ChatMessage message = mapper.readValue(rawJson, ChatMessage.class);
	//		return message;
	//	}

}