package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Chat;
import com.example.demo.entity.Item;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	List<Chat> findByItemAndClientAndOwner(Item item, Account client, Account owner);

}