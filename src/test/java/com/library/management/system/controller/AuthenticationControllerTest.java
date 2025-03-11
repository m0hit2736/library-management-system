package com.library.management.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.system.dto.JwtResponse;
import com.library.management.system.dto.LoginRequest;
import com.library.management.system.dto.UserDTO;
import com.library.management.system.enums.Role;
import com.library.management.system.exception.WeakPasswordException;
import com.library.management.system.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper; // For JSON conversion

	@Test
	void testRegisterUser_Success() throws Exception {
		UserDTO userDTO = new UserDTO("testUser", "password123", "email@email.com", Role.ADMIN.getValue());
		when(userService.register(any(UserDTO.class))).thenReturn(userDTO);

		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testUser"));
	}

	@Test
	void testRegisterUser_WeakPassword() throws Exception {
		UserDTO userDTO = new UserDTO("testUser", "week", "email@email.com", Role.ADMIN.getValue());
		when(userService.register(any(UserDTO.class))).thenThrow(new WeakPasswordException("Weak password"));

		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isBadRequest());
	}

	@Test
	void testLogin_Success() throws Exception {
		LoginRequest loginRequest = new LoginRequest("testUser", "password123");
		JwtResponse jwtResponse = new JwtResponse("fake-jwt-token");

		when(userService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("fake-jwt-token"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = { "ADMIN" })
	void testGetAuthenticatedUser() throws Exception {
		UserDTO userDTO = new UserDTO("testUser", "password123", "email@email.com", Role.ADMIN.getValue());

		when(userService.getAuthenticatedUser("testUser")).thenReturn(userDTO);

		mockMvc.perform(get("/api/auth/me").param("username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testUser"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = { "ADMIN" })
	void testRefreshToken() throws Exception {
		JwtResponse jwtResponse = new JwtResponse("new-fake-jwt-token");

		when(userService.refresh("old-token")).thenReturn(jwtResponse);

		mockMvc.perform(post("/api/auth/refresh").param("oldToken", "old-token")).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("new-fake-jwt-token"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = { "ADMIN" })
	void testLogout() throws Exception {
		doNothing().when(userService).logout("fake-token");

		mockMvc.perform(post("/api/auth/logout").param("token", "fake-token")).andExpect(status().isOk())
				.andExpect(content().string("User logged out successfully"));
	}
}
