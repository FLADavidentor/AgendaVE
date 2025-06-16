package com.uam.agendave.dto.Usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdministradorDTO {

    private UUID id;
    private String username;
    private String correo;
    private String nombre;
    private String apellido;
    @JsonInclude(JsonInclude.Include.NON_NULL) // Excluir si es nulo
    private String contrasena; // Solo útil para entrada, no para salida
}
