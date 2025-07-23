package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByEmailAndPassword(String email, String password);

	Account findByEmail(String email);

	Optional<Account> findById(Long id);
}
