package com.uam.agendave.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstudianteDTO {

    @NotBlank(message = "El CIF es obligatorio")
    private String cif;
    @Email(message = "El correo debe ser valido")
    private String correo;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "La facultad es obligatoria")
    private String facultad;
    @NotBlank(message = "La carrera es obligatoria")
    private String carrera;


}
