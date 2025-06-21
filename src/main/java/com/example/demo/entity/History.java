package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class History {

	@Id
	private Long id;

	protected History() {
	}
}
