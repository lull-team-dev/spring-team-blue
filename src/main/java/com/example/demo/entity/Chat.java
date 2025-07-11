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
@Table(name = "chats")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Account client;
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private Account owner;
	private String message;
	@Column(name = "sent_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime sentAt;

	public Chat() {
	}

	public Chat(Item item, Account client, Account owner) {
		this.item = item;
		this.client = client;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public Account getClient() {
		return client;
	}

	public Account getOwner() {
		return owner;
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

	public void setItem(Item item) {
		this.item = item;
	}

	public void setClient(Account client) {
		this.client = client;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
