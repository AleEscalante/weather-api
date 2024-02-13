package org.consiti.prueba.weather.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUser {
    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotBlank(message = "Username may not be blank")
    private String username;

    @NotBlank(message = "email may not be blank")
    private String email;

    @NotBlank(message = "Password may not be blank")
    private String password;
}
