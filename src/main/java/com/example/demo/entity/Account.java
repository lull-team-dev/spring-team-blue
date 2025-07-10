package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	private String profile;

	@Column(nullable = true)
	private String address;

	private String tel;
	
	@Column(name = "payment_method", nullable = true)
	private String paymentMethod;

	public Account() {
	}

	public Account(Long id, String name, String nickname, String email, String password, String profile,
			String address, String tel) {
		super();
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.address = address;
		this.tel = tel;
	}
	


	// getter
	public Long getId() {
		return id; // Long → Integer に変換（呼び出し側が Integer を期待してるなら）
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickname;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getProfile() {
		return profile;
	}

	public String getAddress() {
		return address;
	}

	public String getTel() {
		return tel;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	// setter
	public void setId(Long id) {
		this.id = id; // Integer → Long に変換
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickName(String nickName) {
		this.nickname = nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
