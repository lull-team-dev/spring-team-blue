package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
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
		List<Chat> unReadchats = messageRepository.findUnreadChats(myId);
		List<Chat> myChats = new ArrayList<>();
		for (Chat myChat : unReadchats) {
			Account account = accountRepository.findById(myId).get();
			if (myChat.getClient() == account || myChat.getOwner() == account) {
				myChats.add(myChat);
			}
		}
		return myChats;
	}

}
