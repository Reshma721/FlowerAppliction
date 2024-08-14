package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.UserService;
import com.floral.floralfiessy.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

//    @BeforeEach
//    public void setUp() {
//        // Create a user and save it to the test database or mock
//        User user = new User();
//        userService.saveUserDetails(user);
//    }

    @Test
    void testGetAllUsersSuccess() {
        UserDto user1 = new UserDto();
        UserDto user2 = new UserDto();
        when(userService.getAllUserDetails()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUserDetails();
    }

    @Test
    void testGetAllUsersFailure() {
        when(userService.getAllUserDetails()).thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getAllUsers();
        });

        assertEquals("Unexpected error", exception.getMessage());
        verify(userService, times(1)).getAllUserDetails();
    }

    @Test
    void testGetUserByIdSuccess() throws UserNotFoundException {
        UserDto userDto = new UserDto();
        when(userService.getUserDetailsById(anyLong())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userService, times(1)).getUserDetailsById(1L);
    }

    @Test
    void testGetUserByIdNotFound() throws UserNotFoundException {
        when(userService.getUserDetailsById(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserDetailsById(1L);
    }

    @Test
    void testAddUserSuccess() {
        UserDto userDto = new UserDto();
        when(userService.create(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userService, times(1)).create(userDto);
    }

    @Test
    void testAddUserFailure() {
        UserDto invalidUser = new UserDto();
        invalidUser.setEmail("invalid-email");

        when(userService.create(any(UserDto.class))).thenThrow(new RuntimeException("Validation failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.createUser(invalidUser);
        });

        assertEquals("Validation failed", exception.getMessage());
        verify(userService, times(1)).create(invalidUser);
    }

    @Test
    void testUpdateUserSuccess() throws UserNotFoundException {
        UserDto userDto = new UserDto();
        when(userService.saveUserDetails(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(1L, userDto);

        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userService, times(1)).saveUserDetails(userDto);
    }

    @Test
    void testUpdateUserNotFound() throws UserNotFoundException {
        UserDto userDto = new UserDto();
        when(userService.saveUserDetails(any(UserDto.class))).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDto> response = userController.updateUser(1L, userDto);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).saveUserDetails(userDto);
    }


    @Test
    void testDeleteUserSuccess() throws UserNotFoundException {
        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("Deleted user 1", response.getBody());
        verify(userService, times(1)).deleteUserDetails(1L);
    }

    @Test
    void testDeleteUserNotFound() throws UserNotFoundException {
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUserDetails(anyLong());

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUserDetails(1L);
    }

}
