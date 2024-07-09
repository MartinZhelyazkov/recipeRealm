package com.example.recipeRealm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotNull(message = "Username can't be null")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;
    @NotNull(message = "Password can't be null")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
            , message = "Password must be at least 8 characters long, contain at least one letter and one digit, and only contain letters and digits")
    private String password;
    @NotNull(message = "Email can't be null")
    @Email
    private String email;
}
