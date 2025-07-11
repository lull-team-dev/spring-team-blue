package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.Item;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  Optional<Bookmark> findByUserAndItem(Account user, Item item);
  void deleteByUserAndItem(Account user, Item item);
}