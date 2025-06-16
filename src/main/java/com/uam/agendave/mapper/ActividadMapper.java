package com.uam.agendave.mapper;

import com.uam.agendave.dto.Actividad.ActividadDTO;
import com.uam.agendave.dto.Actividad.ImagenDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.ImageData;
import com.uam.agendave.model.Lugar;
import com.uam.agendave.model.NombreActividad;
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

        if (actividad.getImagen() != null) {
            ImagenDTO imagenDTO = new ImagenDTO();
            imagenDTO.setNombre(actividad.getImagen().getNombre());
            imagenDTO.setImagenBase64(actividad.getImagen().getImagenBase64());
            dto.setImagen(imagenDTO);
        }

        dto.setLugar(actividad.getLugar() != null ? actividad.getLugar().getNombre() : null);
        dto.setNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getNombre() : null);

        dto.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        dto.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

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

        if (dto.getImagen() != null && dto.getImagen().getImagenBase64() != null && !dto.getImagen().getImagenBase64().isEmpty()) {
            ImageData imageData = new ImageData();
            imageData.setNombre(dto.getImagen().getNombre());
            imageData.setImagenBase64(dto.getImagen().getImagenBase64());
            actividad.setImagen(imageData);
        }

        actividad.setConvalidacionesPermitidas(dto.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(dto.getTotalConvalidacionesPermitidas());

        return actividad;
    }
}
