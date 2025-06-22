package com.uam.agendave.mapper;

import com.uam.agendave.dto.Actividad.TransporteDTO;
import com.uam.agendave.model.Lugar;
import com.uam.agendave.model.Transporte;

public class TransporteMapper {

    // Convierte entidad a DTO (usado en controlador o al mostrar datos)
    public TransporteDTO toDTO(Transporte transporte) {
        if (transporte == null || transporte.getLugar() == null) return null;

        TransporteDTO dto = new TransporteDTO();
        dto.setId(transporte.getId());
        dto.setLugar(transporte.getLugar().getNombre());
        dto.setCapacidad(transporte.getCapacidad());
        return dto;
    }

    // Convierte DTO a entidad, recibiendo el objeto Lugar resuelto previamente en el servicio
    public Transporte toEntity(TransporteDTO dto, Lugar lugar) {
        if (dto == null || lugar == null) return null;

        Transporte transporte = new Transporte();
        transporte.setId(dto.getId()); // útil si estás actualizando
        transporte.setLugar(lugar);    // lugar ya resuelto en el service
        transporte.setCapacidad(dto.getCapacidad());
        return transporte;
    }
}