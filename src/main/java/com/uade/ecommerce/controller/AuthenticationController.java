package com.uade.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecommerce.dto.request.LoginRequest;
import com.uade.ecommerce.dto.request.UsuarioRegisterDTO;
import com.uade.ecommerce.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    //http://localhost:8080/api/auth/register con metodo post http, enviar un body -> crear un usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UsuarioRegisterDTO request) {
        //request tiene los datos del usuario a registrar, como nombre, email y contraseña
        return ResponseEntity.ok(authenticationService.register(request));
    }

    //http://localhost:8080/api/auth/login con metodo post http, enviar un body -> loguear un usuario
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        String token = authenticationService.authenticate(request);

        // 2. Creamos la cookie HTTP-Only con ese token
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Poner en true solo en producción (con HTTPS)
        cookie.setPath("/"); // Disponible para todo el dominio
        cookie.setDomain("localhost");   
        cookie.setMaxAge(24 * 60 * 60); // Duración de 1 día (en segundos)

        // 3. Añadimos la cookie a la respuesta HTTP
        response.addCookie(cookie);

        // 4. Respondemos con un mensaje de éxito. Ya NO mandamos el token en el body.
        return ResponseEntity.ok("Login exitoso. Cookie establecida.");
    }
}
