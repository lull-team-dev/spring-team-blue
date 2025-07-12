package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findByChat(Chat chat);

}
