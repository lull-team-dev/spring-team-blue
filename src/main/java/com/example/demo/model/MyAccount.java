package com.example.demo.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MyAccount {

	private Integer id;

	private String name;

	//getter
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	//setter
	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
