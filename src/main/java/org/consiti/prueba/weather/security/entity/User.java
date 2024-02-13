package org.consiti.prueba.weather.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name may not be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Username may not be blank")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "email may not be blank")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password may not be blank")
    @Column(nullable = false)
    private String password;

    public User(@NotBlank String name, @NotBlank String username, @NotBlank String email, @NotBlank String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
