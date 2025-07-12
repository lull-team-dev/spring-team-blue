package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ChatMessage {
	private String name;
	private String statement;

	public ChatMessage() {
	} // ← これが超重要！

}
