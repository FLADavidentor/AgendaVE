package com.uam.agendave.mapper;

import com.uam.agendave.dto.Actividad.NombreActividadDTO;
import com.uam.agendave.model.NombreActividad;
import org.springframework.stereotype.Component;

@Component
public class NombreActividadMapper {

    public NombreActividadDTO toDTO(NombreActividad nombreActividad) {
        if (nombreActividad == null) return null;

        NombreActividadDTO dto = new NombreActividadDTO();
        dto.setId(nombreActividad.getId());
        dto.setNombre(nombreActividad.getNombre());
        return dto;
    }

    public NombreActividad toEntity(NombreActividadDTO dto) {
        if (dto == null) return null;

        NombreActividad nombreActividad = new NombreActividad();
        nombreActividad.setId(dto.getId());
        nombreActividad.setNombre(dto.getNombre());
        return nombreActividad;
    }
}
