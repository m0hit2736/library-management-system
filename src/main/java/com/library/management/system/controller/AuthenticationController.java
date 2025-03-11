package com.library.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.management.system.dto.JwtResponse;
import com.library.management.system.dto.LoginRequest;
import com.library.management.system.dto.UserDTO;
import com.library.management.system.exception.WeakPasswordException;
import com.library.management.system.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDto) throws WeakPasswordException {
		return ResponseEntity.ok(userService.register(userDto));
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		return ResponseEntity.ok(userService.login(loginRequest));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public ResponseEntity<UserDTO> getAuthenticatedUser(@RequestParam String username) {
		return ResponseEntity.ok(userService.getAuthenticatedUser(username));
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refreshToken(@RequestParam String oldToken) {
		return ResponseEntity.ok(userService.refresh(oldToken));
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestParam String token) {
		userService.logout(token);
		return ResponseEntity.ok("User logged out successfully");
	}
}
