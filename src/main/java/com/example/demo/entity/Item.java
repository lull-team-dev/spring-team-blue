package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_Id", nullable = false)
	private Account account;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private String memo;

	@Column(nullable = false)
	private Integer price;

	@Column(name = "sold_out", nullable = false)
	private Boolean soldOut;

	// DBに保存しない一時的な変数
	@Transient
	// 画面表示用
	private boolean bookmarked;

	public Item() {

	}

	public Item(Long id, Account account, Category category, String name, String image, String memo, Integer price,
			Boolean soldOut) {
		super();
		this.id = id;
		this.account = account;
		this.category = category;
		this.name = name;
		this.image = image;
		this.memo = memo;
		this.price = price;
		this.soldOut = soldOut;
	}

	// getter
	public Long getId() {
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
	
	//	boolean のgetterは → is〇〇() がルール（Javaの決まり）
	public boolean isBookmarked() {
		return bookmarked;
	}

	// setter
	public void setId(Long id) {
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
	
	public void setBookmarked(boolean bookmarked) {
	    this.bookmarked = bookmarked;
	}

}
