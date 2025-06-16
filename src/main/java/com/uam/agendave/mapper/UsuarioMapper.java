package com.uam.agendave.mapper;

import com.uam.agendave.dto.Usuario.AdministradorDTO;
import com.uam.agendave.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public AdministradorDTO toDTO(Usuario entity) {
        AdministradorDTO dto = new AdministradorDTO();
        dto.setCorreo(entity.getCorreo());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        return dto;
    }

    public Usuario toEntity(AdministradorDTO dto) {
        Usuario entity = new Usuario();
        entity.setCorreo(dto.getCorreo());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        return entity;
    }
}
