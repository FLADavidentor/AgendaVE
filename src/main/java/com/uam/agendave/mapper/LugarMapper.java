package com.uam.agendave.mapper;

import com.uam.agendave.dto.LugarDTO;
import com.uam.agendave.model.Lugar;

public class LugarMapper {

    public LugarDTO toDTO(Lugar lugar) {
        LugarDTO dto = new LugarDTO();
        dto.setId(lugar.getId());
        dto.setNombre(lugar.getNombre());
        dto.setCapacidad(lugar.getCapacidad());
        return dto;
    }

    public static Lugar toEntity(LugarDTO dto) {
        Lugar lugar = new Lugar();
        lugar.setId(dto.getId());
        lugar.setNombre(dto.getNombre());
        lugar.setCapacidad(dto.getCapacidad());
        return lugar;
    }
}
