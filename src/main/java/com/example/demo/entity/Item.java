package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 商品ID

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false) // ユーザー（出品者）を参照
	private Account account;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false) // カテゴリーを参照
	private Category category;

	@Column(nullable = false)
	private String name; // 商品名

	@Column(nullable = false)
	private String image; // 画像ファイル名

	@Column(nullable = false)
	private String memo; // 商品説明

	@Column(nullable = false)
	private Integer price; // 価格

	@Column(name = "sold_out", nullable = false)
	private Boolean soldOut; // 売り切れかどうか

	// --- Getter & Setter ---

	public Integer getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getMemo() {
		return memo;
	}

	public Integer getPrice() {
		return price;
	}

	public Boolean getSoldOut() {
		return soldOut;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setSoldOut(Boolean soldOut) {
		this.soldOut = soldOut;
	}
}
