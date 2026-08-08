package com.cesarfrade.ats.dto;

import com.cesarfrade.ats.security.Rol;
import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String email;
    private String password;
    private Rol rol;
}