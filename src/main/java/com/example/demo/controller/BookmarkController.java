package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Account;
import com.example.demo.entity.Item;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.BookmarkService;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {

  @Autowired
  private BookmarkService bookmarkService;

  @Autowired
  HttpSession session;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private MyAccount myAccount;

  @PostMapping("/toggle-ajax")
  @ResponseBody
  public String toggleBookmarkAjax(@RequestParam("itemId") Long itemId) {
    Account account = accountRepository.findById(myAccount.getId()).orElseThrow();
    Item item = itemRepository.findById(itemId).orElseThrow();
    boolean isNowBookmarked = bookmarkService.toggleBookmark(account, item);
    return isNowBookmarked ? "bookmarked" : "unbookmarked";
  }
}