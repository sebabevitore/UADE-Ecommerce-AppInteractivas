package com.uade.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
//este filtro se ejecuta antes de llamar al controller
//fue configurado en SecurityConfig en la instrucción `addFilterBefore`
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Este método se ejecuta en cada petición HTTP para verificar si existe un token JWT válido.
     * Dónde se utiliza:
     * - Se configura en `SecurityConfig` para que se ejecute antes que el filtro de autenticación de Spring Security.
     * - Intercepta todas las peticiones entrantes a la API.
     */
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;

        // 1. INTENTAR BUSCAR EL TOKEN EN LAS COOKIES (Nueva estrategia Avanzada)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. RESPALDO: Si no estaba en la cookie, intentamos buscarlo en el Header tradicional
        if (token == null) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }
        }

        // 3. SI ENCONTRAMOS EL TOKEN (ya sea por Cookie o por Header), VALIDAMOS
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsername(token);
                Set<String> roles = jwtUtil.getRoles(token);

                var authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}