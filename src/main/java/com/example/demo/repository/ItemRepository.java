package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	// SELECT * FROM items WHERE category_id = ?
	List<Item> findByCategoryId(Integer categoryId);

	// SELECT * FROM items WHERE account_id = ?
	List<Item> findByAccount(Account account);

	List<Item> findByNameContaining(String keyword);
}
