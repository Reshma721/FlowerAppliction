package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.resource.Role;
import com.floral.floralfiessy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "John", "Doe", "john_doe", "password", "1234567890", "123 Street", "john@example.com", Role.USER);
    }
    @Test
    public void testFindAll_Success() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstname("Jane");
        user2.setLastname("Doe");
        user2.setUsername("janedoe");
        user2.setEmail("janedoe@example.com");
        user2.setPassword("password");
        user2.setMobno("0987654321");
        user2.setAddress("456 Main St");
        user2.setRole(Role.USER);

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userRepository.findAll();
        assertEquals(2, foundUsers.size());
        assertEquals("John", foundUsers.get(0).getFirstname());
        assertEquals("Jane", foundUsers.get(1).getFirstname());
    }

    @Test
    public void testFindByUsername_Success() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testFindByUsername_Unsuccessful() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testSaveUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    public void testSaveUser_NullValue() {
        User nullUser = new User();
        when(userRepository.save(nullUser)).thenThrow(new IllegalArgumentException("User cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.save(nullUser);
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userRepository.findById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    public void testFindById_Unsuccessful() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> foundUser = userRepository.findById(1L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testDeleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);
        userRepository.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUser_Unsuccessful() {
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(1L);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRepository.deleteById(1L);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userRepository.save(user);
        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
    }

    @Test
    public void testCreateUser_NullValues() {
        User nullUser = new User();
        when(userRepository.save(nullUser)).thenThrow(new IllegalArgumentException("User cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.save(nullUser);
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    public void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = userRepository.save(user);
        assertNotNull(updatedUser);
        assertEquals(user.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void testUpdateUser_Unsuccessful() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (userRepository.findById(1L).isEmpty()) {
                throw new RuntimeException("User not found");
            }
            userRepository.save(user);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDeleteUser_Successful() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userRepository.delete(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UnsuccessfulById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (userRepository.findById(1L).isEmpty()) {
                throw new RuntimeException("User not found");
            }
            userRepository.deleteById(1L);
        });
        assertEquals("User not found", exception.getMessage());
    }
}
