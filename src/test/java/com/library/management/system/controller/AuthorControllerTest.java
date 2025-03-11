package com.library.management.system.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.system.dto.AuthorDTO;
import com.library.management.system.entities.AuthorEntity;
import com.library.management.system.service.AuthorService;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorService authorService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "user")
	void testGetAllAuthors() throws Exception {
		AuthorDTO authorDTO1 = new AuthorDTO("mohit", "gupta", "mg435@gmail.com", null, null);
		AuthorDTO authorDTO2 = new AuthorDTO("rv", "gupta", "rv435@gmail.com", null, null);
		List<AuthorEntity> authors = Arrays.asList(new AuthorEntity(authorDTO1), new AuthorEntity(authorDTO2));

		when(authorService.getAllAuthors()).thenReturn(authors);

		mockMvc.perform(get("/api/authors")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testDeleteAuthor() throws Exception {
		doNothing().when(authorService).deleteAuthor(1L);

		mockMvc.perform(delete("/api/authors/1")).andExpect(status().isOk())
				.andExpect(content().string("Author deleted successfully"));
	}
}
