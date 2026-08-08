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

    // Usamos una clave secreta para firmar los tokens.
    // Lo ideal es ponerla en application.yml, pero le damos un valor por defecto para empezar.
    @Value("${jwt.secret}")
    private String secretKey;

    // Tiempo de vida del token: 24 horas (en milisegundos)
    private static final long TIEMPO_EXPIRACION = 86400000;

    /**
     * Fabrica un Token nuevo cuando el usuario hace login correctamente.
     */
    public String generarToken(Usuario usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(usuario.getEmail()) // El "dueño" del token es el email
                .withClaim("rol", usuario.getRol().name()) // Guardamos su rol dentro del token
                .withIssuedAt(new Date()) // Fecha de creación
                .withExpiresAt(new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION)) // Fecha de caducidad
                .sign(algoritmo); // Lo firmamos criptográficamente
    }

    /**
     * Lee un Token que nos llega en una petición, comprueba que no sea falso ni esté caducado,
     * y extrae el email del usuario.
     */
    public String validarTokenYObtenerEmail(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secretKey);

            return JWT.require(algoritmo)
                    .build()
                    .verify(token) // Si el token es falso o caducó, esto lanza una excepción
                    .getSubject(); // Devuelve el email

        } catch (JWTVerificationException exception) {
            System.out.println("Token inválido o caducado: " + exception.getMessage());
            return null;
        }
    }
}
