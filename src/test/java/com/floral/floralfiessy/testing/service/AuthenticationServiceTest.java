package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.response.AuthenticationResponse;
import com.floral.floralfiessy.service.AuthenticationService;
import com.floral.floralfiessy.service.JwtService;
import com.floral.floralfiessy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setEmail("john.doe@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setUsername("johndoe");
        userDto.setPassword("password");
        userDto.setEmail("john.doe@example.com");
    }

    @Test
    void register_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthenticationResponse response = authenticationService.register(user);

        assertEquals("token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Failure() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Failed to save user"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(user);
        });

        assertEquals("Failed to save user", exception.getMessage());
    }

    @Test
    void login_Success() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("johndoe");
        when(userDetails.getPassword()).thenReturn("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthenticationResponse response = authenticationService.login(userDetails);

        assertEquals("token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_BadCredentials() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("johndoe");
        when(userDetails.getPassword()).thenReturn("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(userDetails);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
