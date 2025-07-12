package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
	private String name;
	private String statement;
	private Long chatId;
	private Long itemId;
	private Long myAccountId;

	public ChatMessage() {
	} // ← これが超重要！

}
