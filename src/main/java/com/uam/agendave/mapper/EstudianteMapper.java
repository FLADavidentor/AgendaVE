package com.uam.agendave.mapper;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.TestDTO;
import com.uam.agendave.model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;
    public EstudianteDTO toDTO(Estudiante entity) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setCif(entity.getCif());
        dto.setCorreo(entity.getCorreo());
        dto.setNombre(entity.getNombres());
        dto.setApellido(entity.getApellidos());
        dto.setCarrera(entity.getCarrera());
        dto.setFacultad(entity.getFacultad());
        return dto;
    }

    public Estudiante toEntity(TestDTO dto,String rawPassword) {
        Estudiante estudiante = new Estudiante();
        estudiante.setCif(dto.getCif());
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setCorreo(dto.getCorreo() != null ? dto.getCorreo().toString() : "");
        estudiante.setTelefono(dto.getTelefono() != null ? dto.getTelefono().toString() : "");
        estudiante.setSexo(dto.getSexo() != null ? dto.getSexo().toString() : "");
        estudiante.setTipo(dto.getTipo() != null ? dto.getTipo().toString() : "");
        estudiante.setFacultad(dto.getFacultad() != null ? dto.getFacultad().toString() : "");
        estudiante.setCarrera(dto.getCarrera() != null ? dto.getCarrera().toString() : "");
        estudiante.setPassword(passwordEncoder.encode(rawPassword));
        return estudiante;
    }
}
