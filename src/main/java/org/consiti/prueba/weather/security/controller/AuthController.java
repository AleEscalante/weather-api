package org.consiti.prueba.weather.security.controller;

import jakarta.validation.Valid;
import org.consiti.prueba.weather.dto.Message;
import org.consiti.prueba.weather.security.dto.JwtDto;
import org.consiti.prueba.weather.security.dto.NewUser;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.service.UserService;
import org.consiti.prueba.weather.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;


    @PostMapping("/sign-up")
    public ResponseEntity<Message> nuevo(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message("Verifique los datos introducidos"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByUsername(newUser.getName())) {
            return new ResponseEntity<>(new Message("El username " + newUser.getUsername() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(newUser.getEmail())) {
            return new ResponseEntity<>(new Message("El email " + newUser.getEmail() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
        }
        User usuario = new User(
                newUser.getName(),
                newUser.getUsername(),
                newUser.getEmail(),
                passwordEncoder.encode(newUser.getPassword()));

        userService.save(usuario);
        return new ResponseEntity<>(new Message("Usuario registrado con exito"), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = TokenUtils.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
