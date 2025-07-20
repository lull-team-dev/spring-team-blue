package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.Account;
import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private MyAccount myAccount;

	@Test
	void testRegisterWithMissingNameShouldReturnValidationError() throws Exception {
		mockMvc.perform(post("/account/register")
				.param("last_name", "")
				.param("first_name", "")
				.param("nickName", "テストユーザー")
				.param("email", "test@example.com")
				.param("password", "pass1234")
				.param("confirmPass", "pass1234")
				.param("zip1", "123")
				.param("zip2", "4567")
				.param("prefecture", "東京都")
				.param("city", "渋谷区")
				.param("town", "道玄坂")
				.param("building", "101ビル")
				.param("tel", "08012345678"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("nameMessage"))
				.andExpect(view().name("account/register"));
	}

	@Test
	void testRegisterWithPasswordMismatchShouldReturnValidationError() throws Exception {
		mockMvc.perform(post("/account/register")
				.param("last_name", "山田")
				.param("first_name", "太郎")
				.param("nickName", "ヤマタロ")
				.param("email", "test@example.com")
				.param("password", "pass1234")
				.param("confirmPass", "different")
				.param("zip1", "123")
				.param("zip2", "4567")
				.param("prefecture", "東京都")
				.param("city", "渋谷区")
				.param("town", "道玄坂")
				.param("building", "101ビル")
				.param("tel", "08012345678"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("passwordMismatchMessage"))
				.andExpect(view().name("account/register"));
	}

	@Test
	void testRegisterWithValidInputShouldRedirect() throws Exception {
		mockMvc.perform(post("/account/register")
				.param("last_name", "山田")
				.param("first_name", "太郎")
				.param("nickName", "ヤマタロ")
				.param("email", "valid@example.com")
				.param("password", "pass1234")
				.param("confirmPass", "pass1234")
				.param("zip1", "123")
				.param("zip2", "4567")
				.param("prefecture", "東京都")
				.param("city", "渋谷区")
				.param("town", "道玄坂")
				.param("building", "101ビル")
				.param("tel", "08012345678"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/account/register/confirm"));
	}

	@Test
	void testLoginWithEmptyEmailShouldReturnValidationError() throws Exception {
		mockMvc.perform(post("/account/login")
				.param("email", "")
				.param("password", "pass1234"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("emailMessage"))
				.andExpect(view().name("account/login"));
	}

	@Test
	void testLoginWithInvalidEmailFormatShouldReturnValidationError() throws Exception {
		mockMvc.perform(post("/account/login")
				.param("email", "invalid-email")
				.param("password", "pass1234"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("emailMessage"))
				.andExpect(view().name("account/login"));
	}

	@Test
	void testLoginWithEmptyPasswordShouldReturnValidationError() throws Exception {
		mockMvc.perform(post("/account/login")
				.param("email", "test@example.com")
				.param("password", ""))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("passwordMessage"))
				.andExpect(view().name("account/login"));
	}

	@Test
	void testLoginWithWrongCredentialsShouldReturnLoginError() throws Exception {
		when(accountRepository.findByEmailAndPassword("test@example.com", "wrongpass"))
				.thenReturn(null); // 認証失敗を模擬

		mockMvc.perform(post("/account/login")
				.param("email", "test@example.com")
				.param("password", "wrongpass"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("loginError"))
				.andExpect(view().name("account/login"));
	}

	@Test
	void testLoginWithCorrectCredentialsShouldRedirect() throws Exception {
		Account dummyAccount = new Account();
		dummyAccount.setId((long) 1);
		dummyAccount.setName("山田太郎");
		dummyAccount.setNickname("たろう");
		dummyAccount.setIcon("default.png");
		dummyAccount.setEmail("test@example.com");
		dummyAccount.setPassword("pass1234");

		when(accountRepository.findByEmailAndPassword("test@example.com", "pass1234"))
				.thenReturn(dummyAccount);

		mockMvc.perform(post("/account/login")
				.param("email", "test@example.com")
				.param("password", "pass1234"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/items"));
	}

}
