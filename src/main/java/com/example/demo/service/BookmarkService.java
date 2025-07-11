package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.Item;
import com.example.demo.repository.BookmarkRepository;

@Service
public class BookmarkService {

  @Autowired
  private BookmarkRepository bookmarkRepository;

  public boolean isBookmarked(Account user, Item item) {
    return bookmarkRepository.findByUserAndItem(user, item).isPresent();
  }

  public boolean toggleBookmark(Account user, Item item) {
	    Optional<Bookmark> existing = bookmarkRepository.findByUserAndItem(user, item);
	    if (existing.isPresent()) {
	        bookmarkRepository.delete(existing.get());
	        return false; // 今はブックマークしてない
	    } else {
	        Bookmark bookmark = new Bookmark();
	        bookmark.setUser(user);
	        bookmark.setItem(item);
	        bookmarkRepository.save(bookmark);
	        return true; // 今ブックマークした
	    }
	}
}