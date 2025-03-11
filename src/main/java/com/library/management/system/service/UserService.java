package com.library.management.system.service;

import java.util.List;

import com.library.management.system.dto.JwtResponse;
import com.library.management.system.dto.LoginRequest;
import com.library.management.system.dto.UserDTO;
import com.library.management.system.exception.WeakPasswordException;

public interface UserService {
	UserDTO register(UserDTO userDTO) throws WeakPasswordException;

	JwtResponse login(LoginRequest loginRequest);

	JwtResponse refresh(String oldToken);

	void logout(String token);

	UserDTO getAuthenticatedUser(String username);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long id);

	UserDTO updateUser(Long id, UserDTO userDTO);

	void deleteUser(Long id);

	void changeUserRole(Long id, String role);
}
