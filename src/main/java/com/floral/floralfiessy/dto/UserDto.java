package com.floral.floralfiessy.dto;

import com.floral.floralfiessy.resource.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    @NotBlank(message = "name must be provided")
    private String firstname;
    @NotBlank(message = "Username must be provided")
    private String lastname;
    @NotBlank(message = "username shouldn't be blank")
    private String username;
    @NotBlank(message = "Password must be provided")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotBlank(message = "Email must be provided")
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "invalid mob number")
    private String mobno;
    @NotBlank(message = "address shouldn't be blank")
    private String address;
    @NotNull
    private Role role;

}
