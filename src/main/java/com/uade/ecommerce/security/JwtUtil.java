package com.uade.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
//Esta clase hace todo lo relacionado con el token.
// Componente encargado de generar, leer y validar tokens JWT.
@Component
public class JwtUtil {
    // Secreto definido en application.properties para firmar y verificar los tokens.
    @Value("${jwt.secret}")
    private String secret;

    // Duración del token en milisegundos, definida en application.properties.
    @Value("${jwt.expiration}")
    private Long expiration; //define cuanto dura el token

    // Convierte el secreto configurado en una clave válida para el algoritmo HS256.
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // CREA TOKEN después del login con: usuario, sus roles y la fecha de vencimiento.
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                // Guarda el email del usuario como identificador principal del token.
                .setSubject(username)
                // Guarda los roles separados por comas, por ejemplo: ROLE_USER,ROLE_ADMIN.
                .claim("roles", String.join(",", roles))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // Firma el token para detectar cualquier modificación posterior.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtiene del token el email guardado como subject.
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Recupera los roles del token y los convierte en un conjunto de strings.
    public Set<String> getRoles(String token) {
        String roles = (String) getClaims(token).get("roles");
        return Set.of(roles.split(","));
    }

    // Devuelve true solo si el token tiene una firma válida y todavía no venció.
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            // La librería ya controla la expiración; esta comparación deja la regla explícita.
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // Incluye tokens vencidos, alterados o con un formato incorrecto.
            return false;
        }
    }

    // Verifica la firma y la expiración; si son válidas, devuelve los datos del token.
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
