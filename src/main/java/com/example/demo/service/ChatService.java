package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Chat;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MessageRepository;

@Service
public class ChatService {

	@Autowired
	MyAccount myAccount;
	@Autowired
	ChatRepository chatRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	AccountRepository accountRepository;

	public List<Chat> getUnreadChatsForCurrentUser() {
		Long myId = myAccount.getId(); // ← セッションからID取得
		return messageRepository.findUnreadChats(myId);
	}

}
