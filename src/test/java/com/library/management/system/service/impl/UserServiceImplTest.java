package com.library.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.library.management.system.utils.JwtTokenProvider;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserBuilder userBuilder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test User Registration
     */
    @Test
    void testRegisterUser_Success() throws WeakPasswordException {
        UserDTO userDTO = new UserDTO("mohit", "P@ssw0rd!", "john@example.com", "ADMIN");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword("encodedPassword");
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setRole(Role.ADMIN);

        when(userBuilder.buildUserEntity(any(UserDTO.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userBuilder.buildUserDTO(anyString())).thenReturn(UserDTO.builder().message("User Registered successfully").build());

        UserDTO result = userService.register(userDTO);

        assertNotNull(result);
    }

    @Test
    void testRegisterUser_WeakPasswordException() {
        UserDTO userDTO = new UserDTO("mohit", "weak", "john@example.com", "ADMIN");

        assertThrows(WeakPasswordException.class, () -> userService.register(userDTO));
    }

    /**
     * Test User Login
     */
    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("mohit", "P@ssw0rd!");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginRequest.getUsername());
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(userEntity)).thenReturn("jwt-token");

        JwtResponse response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("mohit", "wrongPassword");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginRequest.getUsername());
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(false);

        assertThrows(InvalidCredsException.class, () -> userService.login(loginRequest));
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest("unknown_user", "password");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> userService.login(loginRequest));
    }

    /**
     * Test Get User by ID
     */
    @Test
    void testGetUserById_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("mohit");
        userEntity.setEmail("john@example.com");
        userEntity.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userBuilder.buildUserDTOForUser(userEntity)).thenReturn(new UserDTO("mohit", "", "john@example.com", "USER"));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("mohit", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> userService.getUserById(1L));
    }

    /**
     * Test Get All Users
     */
    @Test
    void testGetAllUsers_Success() {
        UserEntity user1 = new UserEntity(1L, "moht", "password", "moht@example.com", Role.USER, true, null);
        UserEntity user2 = new UserEntity(2L,"admin_user", "password", "admin@example.com", Role.ADMIN, true, null);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userBuilder.buildUserDTOForUser(user1)).thenReturn(new UserDTO("moht", "", "moht@example.com", "USER"));
        when(userBuilder.buildUserDTOForUser(user2)).thenReturn(new UserDTO("admin_user", "", "admin@example.com", "ADMIN"));

        List<UserDTO> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    /**
     * Test Update User
     */
    @Test
    void testUpdateUser_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("old_username");
        userEntity.setEmail("old@example.com");
        userEntity.setRole(Role.USER);

        UserDTO updatedUserDTO = new UserDTO("new_username", "", "new@example.com", "ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userBuilder.buildUserDTO(anyString())).thenReturn(UserDTO.builder().message("User Registered successfully").build());

        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertNotNull(result);
    }

    /**
     * Test Delete User
     */
    @Test
    void testDeleteUser_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).delete(userEntity);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    /**
     * Test Change User Role
     */
    @Test
    void testChangeUserRole_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        assertDoesNotThrow(() -> userService.changeUserRole(1L, "ADMIN"));
    }
}
