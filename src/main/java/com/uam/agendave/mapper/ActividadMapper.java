package com.uam.agendave.mapper;

import com.uam.agendave.dto.Actividad.ActividadDTO;
import com.uam.agendave.dto.Actividad.ImagenDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Lugar;
import com.uam.agendave.model.NombreActividad;
import com.uam.agendave.model.Transporte;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapper {

    public ActividadDTO toDTO(Actividad actividad) {
        if (actividad == null) return null;

        ActividadDTO dto = new ActividadDTO();
        dto.setId(actividad.getId());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setFecha(actividad.getFecha());
        dto.setHoraInicio(actividad.getHoraInicio());
        dto.setHoraFin(actividad.getHoraFin());
        dto.setEstado(actividad.isEstado());
        dto.setCupo(actividad.getCupo());

        // ⬇️ set image filename only (frontend will load it with backendFiles/images/[filename])
        if (actividad.getImagenPath() != null && !actividad.getImagenPath().isBlank()) {
            ImagenDTO imagenDTO = new ImagenDTO();
            imagenDTO.setNombre(actividad.getImagenPath()); // filename
            dto.setImagen(imagenDTO);
        }

        dto.setLugar(actividad.getLugar() != null ? actividad.getLugar().getNombre() : null);
        dto.setNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getNombre() : null);

        dto.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        dto.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        if (actividad.getTransporte() != null) {
            dto.setIdTransporte(actividad.getTransporte().getId());
        }

        return dto;
    }

    public Actividad toEntity(ActividadDTO dto, NombreActividad nombreActividad, Lugar lugar) {
        if (dto == null) return null;

        Actividad actividad = new Actividad();
        actividad.setId(dto.getId());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setFecha(dto.getFecha());
        actividad.setHoraInicio(dto.getHoraInicio());
        actividad.setHoraFin(dto.getHoraFin());
        actividad.setEstado(dto.isEstado());
        actividad.setCupo(dto.getCupo());
        actividad.setNombreActividad(nombreActividad);
        actividad.setLugar(lugar);

        // ❌ don't map imagenBase64 here
        // that logic is handled in the service when saving or updating the entity

        actividad.setConvalidacionesPermitidas(dto.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(dto.getTotalConvalidacionesPermitidas());

        return actividad;
    }

    public Actividad toEntity(ActividadDTO dto, NombreActividad nombreActividad, Lugar lugar, Transporte transporte) {
        Actividad actividad = toEntity(dto, nombreActividad, lugar);
        actividad.setTransporte(transporte);
        return actividad;
    }
}
