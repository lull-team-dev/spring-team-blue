package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookmarks")
public class Bookmark {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Account user;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  // --- Getter ---
  public Long getId() {
    return id;
  }

  public Account getUser() {
    return user;
  }

  public Item getItem() {
    return item;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  // --- Setter ---
  public void setId(Long id) {
    this.id = id;
  }

  public void setUser(Account user) {
    this.user = user;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}