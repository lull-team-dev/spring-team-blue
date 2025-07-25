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
@Table(name = "history")
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// users テーブルとのリレーション
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Account account;

	// items テーブルとのリレーション
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(nullable = false)
	private LocalDateTime date;

	@Column(name = "total_price", nullable = false)
	private Integer totalPrice;

	public History() {

	}

	public History(Account account, Item item, LocalDateTime date, Integer totalPrice) {
		this.account = account;
		this.item = item;
		this.date = date;
		this.totalPrice = totalPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
}
