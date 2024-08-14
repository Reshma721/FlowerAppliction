package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.resource.Role;
import com.floral.floralfiessy.service.UserService;
import com.floral.floralfiessy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "John", "Doe", "john_doe", "password", "1234567890", "123 Street", "john@example.com", Role.USER);
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setUsername("john_doe");
        userDto.setPassword("password");
        userDto.setEmail("john@example.com");
        userDto.setMobno("1234567890");
        userDto.setAddress("123 Street");
        userDto.setRole(Role.USER);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername("john_doe");
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("john_doe");
        });
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto createdUser = userService.create(userDto);
        assertNotNull(createdUser);
        assertEquals(userDto.getUsername(), createdUser.getUsername());
    }

    @Test
    public void testGetAllUserDetails_Success() {
        List<User> userList = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> result = userService.getAllUserDetails();
        assertEquals(1, result.size());
        assertEquals(userDto.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetUserDetailsById_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto result = userService.getUserDetailsById(1L);
        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
    }

    @Test
    public void testGetUserDetailsById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserDetailsById(1L);
        });
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException {
        long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldusername");

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("newusername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updatedUserDto = userService.saveUserDetails(userDto);

        assertEquals("newusername", updatedUserDto.getUsername());
    }
    @Test
    public void testSaveUserDetails_NotFound() throws UserNotFoundException{
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(Exception.class, () -> {
                userService.saveUserDetails(userDto);
            });
        }


    @Test
    public void testSaveUserDetails_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto result = userService.saveUserDetails(userDto);
        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
    }

    @Test
    public void testDeleteUserDetails_Success() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userService.deleteUserDetails(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserDetails_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserDetails(1L);
        });
    }
}
