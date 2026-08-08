package com.cesarfrade.ats.security;

import com.cesarfrade.ats.repository.UsuarioRepository;
import com.cesarfrade.ats.service.IUsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Miramos si la petición trae la cabecera Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Comprobamos que el token exista y empiece por "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7); // Extraemos solo el token
            String email = jwtUtil.validarTokenYObtenerEmail(jwt); // Validamos que no sea falso

            // 3. Si el token es real y el usuario no está ya logueado...
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                try {
                    // Usamos el Service con el método oficial de Spring Security
                    UserDetails userDetails = usuarioService.loadUserByUsername(email);

                    // Creamos su "acreditación" oficial
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Le ponemos la pulsera VIP en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } catch (Exception e) {
                    // Si el token es válido pero el usuario fue borrado de la base de datos
                    System.out.println("Usuario no encontrado en la base de datos: " + email);
                }
            }
        }

        // 4. Dejamos que la petición siga su camino
        filterChain.doFilter(request, response);
    }
}