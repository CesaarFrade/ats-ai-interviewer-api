package com.cesarfrade.ats.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cesarfrade.ats.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // Tiempo de vida del token: 24 horas (en milisegundos)
    private static final long TIEMPO_EXPIRACION = 86400000;

    public String generarToken(Usuario usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("rol", usuario.getRol().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION))
                .sign(algoritmo);
    }

    public String validarTokenYObtenerEmail(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secretKey);

            return JWT.require(algoritmo)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            System.out.println("Token inválido o caducado: " + exception.getMessage());
            return null;
        }
    }
}
