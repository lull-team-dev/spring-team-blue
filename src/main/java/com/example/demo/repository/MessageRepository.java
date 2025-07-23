package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Chat;
import com.example.demo.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findByChat(Chat chat);

	@Query("SELECT DISTINCT m.chat FROM Message m " +
			"WHERE m.isRead = false AND m.sender.id <> :myAccountId")
	List<Chat> findUnreadChats(@Param("myAccountId") Long myAccountId);

}
