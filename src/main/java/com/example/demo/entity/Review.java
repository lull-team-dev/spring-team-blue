package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Review {

	@Id
	private Long id;

	protected Review() {
	}
}
