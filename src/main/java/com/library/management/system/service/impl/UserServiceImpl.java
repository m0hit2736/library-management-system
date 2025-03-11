package com.library.management.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.management.system.builder.UserBuilder;
import com.library.management.system.dto.JwtResponse;
import com.library.management.system.dto.LoginRequest;
import com.library.management.system.dto.UserDTO;
import com.library.management.system.entities.UserEntity;
import com.library.management.system.enums.Role;
import com.library.management.system.exception.InvalidCredsException;
import com.library.management.system.exception.RecordNotFoundException;
import com.library.management.system.exception.WeakPasswordException;
import com.library.management.system.repository.UserRepository;
import com.library.management.system.service.UserService;
import com.library.management.system.utils.JwtTokenProvider;
import com.library.management.system.utils.PasswordValidator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserBuilder userBuilder;

	private static final String USER_NOT_FOUND = "User Not Found";

	@Override
	public UserDTO register(UserDTO userDTO) throws WeakPasswordException {
		if (!PasswordValidator.isValidPassword(userDTO.getPassword())) {
			throw new WeakPasswordException(
					"Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a digit, and a special character.");
		}

		UserEntity user = userBuilder.buildUserEntity(userDTO);
		userRepository.save(user);
		return userBuilder.buildUserDTO("User Registered succesfully");
	}

	@Override
	public JwtResponse login(LoginRequest loginRequest) {
		UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new InvalidCredsException("Invalid credentials");
		}

		String token = jwtTokenProvider.generateToken(user);
		return new JwtResponse(token);
	}

	/**
	 * Refresh JWT Token
	 */
	public JwtResponse refresh(String oldToken) {
		String username = jwtTokenProvider.getUsernameFromToken(oldToken);
		if (username == null || !jwtTokenProvider.validateToken(oldToken)) {
			throw new RuntimeException("Invalid or expired token");
		}
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));

		String newToken = jwtTokenProvider.generateToken(user);
		return new JwtResponse(newToken);
	}

	/**
	 * Logout user (invalidate the token - assuming token blacklist implementation)
	 */
	public void logout(String token) {
		jwtTokenProvider.invalidateToken(token);
	}

	/**
	 * Get currently logged-in user details
	 */
	public UserDTO getAuthenticatedUser(String username) {
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));
		return new UserDTO(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole().name());
	}

	/**
	 * Get all users (Admin only)
	 */
	public List<UserDTO> getAllUsers() {
		List<UserEntity> users = userRepository.findAll();
		return users.stream().map(user -> userBuilder.buildUserDTOForUser(user)).toList();
	}

	/**
	 * Get a user by ID (Admin only)
	 */
	public UserDTO getUserById(Long id) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));
		return userBuilder.buildUserDTOForUser(user);
	}

	/**
	 * Update user (Admin only)
	 */
	public UserDTO updateUser(Long id, UserDTO userDTO) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));

		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setRole(Role.getByValue(userDTO.getRole()));
		userRepository.save(user);

		return userBuilder.buildUserDTO("User Updated Successfully");
	}

	/**
	 * Delete user (Admin only)
	 */
	public void deleteUser(Long id) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));
		userRepository.delete(user);
	}

	/**
	 * Change user role (Admin only)
	 */
	public void changeUserRole(Long id, String role) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(USER_NOT_FOUND));
		user.setRole(Role.getByValue(role));
		userRepository.save(user);
	}
}
