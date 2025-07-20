package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import com.example.demo.model.MyAccount;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.ChatService;
import com.example.demo.service.ItemService;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("removal")
	@MockBean
	private AccountRepository accountRepository;

	@SuppressWarnings("removal")
	@MockBean
	private CategoryRepository categoryRepository;

	@SuppressWarnings("removal")
	@MockBean
	private ItemRepository itemRepository;

	@SuppressWarnings("removal")
	@MockBean
	private ItemService itemService;

	@SuppressWarnings("removal")
	@MockBean
	private MyAccount myAccount;

	@SuppressWarnings("removal")
	@MockBean
	private ChatService chatService;

	@SuppressWarnings("removal")
	@MockBean
	private BookmarkRepository bookmarkRepository;

	@BeforeEach
	void setup() {
		// ログイン済みユーザーを模倣
		given(myAccount.getId()).willReturn(1);
		given(myAccount.getName()).willReturn("テストユーザー");
		// 必要に応じて他の情報もここに追加
	}

	@Test
	void testSubmitItemWithEmptyNameShouldReturnValidationError() throws Exception {
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "")
				.param("price", "1000")
				.param("category_id", "1")
				.param("memo", "これはメモです"))
						.file(new MockMultipartFile("image_file", "test.jpg", "image/jpeg", new byte[1])))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("itemNameMessage"))
				.andExpect(view().name("item/item_new"));
	}

	@Test
	void testSubmitItemWithTooLongNameShouldReturnValidationError() throws Exception {
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "ああああああああああああああああああああああああああああああああ")
				.param("price", "1000")
				.param("category_id", "1")
				.param("memo", "これはメモです"))
						.file(new MockMultipartFile("image_file", "test.jpg", "image/jpeg", new byte[1])))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("itemNameMessage"))
				.andExpect(view().name("item/item_new"));
	}

	@Test
	void testSubmitItemWithInvalidPriceShouldReturnValidationError() throws Exception {
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "商品")
				.param("price", "0")
				.param("category_id", "1")
				.param("memo", "これはメモです"))
						.file(new MockMultipartFile("image_file", "test.jpg", "image/jpeg", new byte[1])))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("priceMessage"))
				.andExpect(view().name("item/item_new"));
	}

	@Test
	void testSubmitItemWithInvalidCategoryShouldReturnValidationError() throws Exception {
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "商品")
				.param("price", "1000")
				.param("category_id", "0")
				.param("memo", "これはメモです"))
						.file(new MockMultipartFile("image_file", "test.jpg", "image/jpeg", new byte[1])))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("categoryMessage"))
				.andExpect(view().name("item/item_new"));
	}

	@Test
	void testSubmitItemWithTooLongMemoShouldReturnValidationError() throws Exception {
		String longMemo = "a".repeat(501);
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "商品")
				.param("price", "1000")
				.param("category_id", "1")
				.param("memo", longMemo))
						.file(new MockMultipartFile("image_file", "test.jpg", "image/jpeg", new byte[1])))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("memoMessage"))
				.andExpect(view().name("item/item_new"));
	}

	@Test
	void testSubmitItemWithNoImageShouldReturnValidationError() throws Exception {
		mockMvc.perform(((MockMultipartHttpServletRequestBuilder) multipart("/items/submit")
				.param("item_name", "商品")
				.param("price", "1000")
				.param("category_id", "1")
				.param("memo", "これはメモです"))
						.file(new MockMultipartFile("image_file", "", "application/octet-stream", new byte[0]))) // ← ここが必要！
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("imageMessage"))
				.andExpect(view().name("item/item_new"));
	}
}
