package com.library.management.system.builder;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.library.management.system.dto.UserDTO;
import com.library.management.system.entities.UserEntity;
import com.library.management.system.enums.Role;

@Component
public class UserBuilder {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserEntity buildUserEntity(UserDTO userDTO) {
		UserEntity user = new UserEntity();
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		user.setRole(Role.getByValue(userDTO.getRole()));
		user.setEnabled(true);
		return user;
	}

	public UserDTO buildUserDTO(String string) {
		return UserDTO.builder().message(string).build();
	}

	public UserDTO buildUserDTOForUser(UserEntity user) {
		return UserDTO.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword())
				.email(user.getEmail()).role(user.getRole().getValue()).build();
	}

}
