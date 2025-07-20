package com.example.demo.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MyAccount {

	private Long id;
	private String name;
	private String nickname;
	private String icon; // ← アイコン画像ファイル名

	// getter
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNickname() {
		return nickname;
	}

	public String getIcon() {
		return icon;
	}

	// setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
