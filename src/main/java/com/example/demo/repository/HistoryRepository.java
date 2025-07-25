package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

	List<History> findByAccount(Account account);

}
