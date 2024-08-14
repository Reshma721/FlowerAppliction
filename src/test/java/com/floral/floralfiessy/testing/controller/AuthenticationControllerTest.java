package com.floral.floralfiessy.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floral.floralfiessy.controller.AuthenticationController;
import com.floral.floralfiessy.dto.UserDto;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.entity.UserMapper;
import com.floral.floralfiessy.resource.Role;
import com.floral.floralfiessy.response.AuthenticationResponse;
import com.floral.floralfiessy.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserMapper userMapper;

    private UserDto userDto;
    private User user;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();

        userDto = new UserDto();
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setUsername("johndoe");
        userDto.setPassword("password123");
        userDto.setEmail("john.doe@example.com");
        userDto.setMobno("1234567890");
        userDto.setAddress("123 Main St");
        userDto.setRole(Role.USER);

        user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setEmail("john.doe@example.com");
        user.setMobno("1234567890");
        user.setAddress("123 Main St");
        user.setRole(Role.USER);

        authenticationResponse = new AuthenticationResponse("dummyToken");
    }

    @Test
    void register_Success() throws Exception {
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(authenticationService.register(any(User.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/loginregister/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"token\":\"dummyToken\"}"));
    }

    @Test
    void register_InvalidInput() throws Exception {
        UserDto invalidUserDto = new UserDto(); // Missing required fields

        mockMvc.perform(post("/loginregister/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_Success() throws Exception {
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(authenticationService.login(any(User.class))).thenReturn(authenticationResponse);

        mockMvc.perform(post("/loginregister/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"dummyToken\"}"));
    }

    @Test
    void login_InvalidInput() throws Exception {
        UserDto invalidUserDto = new UserDto(); // Missing required fields

        mockMvc.perform(post("/loginregister/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest());
    }
}