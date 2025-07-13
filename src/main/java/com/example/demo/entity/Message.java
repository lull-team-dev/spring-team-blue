package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 紐づくチャットルーム
	@ManyToOne
	@JoinColumn(name = "chat_id", nullable = false)
	private Chat chat;

	// メッセージの送信者
	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private Account sender;

	// メッセージ本文
	@Column(nullable = false)
	private String message;

	// 送信日時
	private LocalDateTime sentAt = LocalDateTime.now();

	public Message() {
	}

	public Message(Chat chat, Account sender, String message, LocalDateTime sentAt) {
		super();
		this.chat = chat;
		this.sender = sender;
		this.message = message;
		this.sentAt = sentAt;
	}

	public Long getId() {
		return id;
	}

	public Chat getChat() {
		return chat;
	}

	public Account getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public void setSender(Account sender) {
		this.sender = sender;
	}

	public void setMesasge(String message) {
		this.message = message;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

}
