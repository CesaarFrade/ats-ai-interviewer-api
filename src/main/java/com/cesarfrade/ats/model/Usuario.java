package com.cesarfrade.ats.model;

import com.cesarfrade.ats.security.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El email será el "nombre de usuario" para hacer login
    @Column(unique = true, nullable = false)
    private String email;

    // Aquí guardaremos la contraseña (¡siempre encriptada, nunca en texto plano!)
    @Column(nullable = false)
    private String password;

    // El rol que creamos en el paso anterior
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // MÉTODOS OBLIGATORIOS DE LA INTERFAZ UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Le pasamos a Spring Security el rol del usuario
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() {
        return this.email; // Usamos el email como identificador principal
    }

    // Estos métodos le dicen a Spring que la cuenta está activa y no está bloqueada
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}