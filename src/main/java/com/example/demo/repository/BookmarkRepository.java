package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.Item;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  // ブックマーク済みか確認
  Optional<Bookmark> findByUserAndItem(Account user, Item item);

  // ブックマークから削除
  void deleteByUserAndItem(Account user, Item item);

  // ブックマーク一覧表示
  List<Bookmark> findAllByUser(Account user);
}