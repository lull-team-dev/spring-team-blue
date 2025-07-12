package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	// メッセージ一覧（1対多）
	@OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	public Chat() {
	}

	public Chat(Item item, Account client, Account owner) {
		this.item = item;
		this.client = client;
		this.owner = owner;
	}

	public Chat(Item item, Account client, Account owner, LocalDateTime createdAt) {
		super();
		this.item = item;
		this.client = client;
		this.owner = owner;
		this.createdAt = createdAt;
	}

	//getter
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

	public List<Message> getMessages() {
		return messages;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	//setter
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

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
