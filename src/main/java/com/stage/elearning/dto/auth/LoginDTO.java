package com.stage.elearning.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginDTO {

    @Pattern(regexp = "[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$", message = "Invalid email address. Please enter a valid email.")
    private String email;
    @NotBlank
    private String password;


    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

}
