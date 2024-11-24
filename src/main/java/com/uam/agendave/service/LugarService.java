package com.uam.agendave.service;

import com.uam.agendave.dto.LugarDTO;

import java.util.List;
import java.util.UUID;

public interface LugarService {
    List<LugarDTO> obtenerTodos();
    LugarDTO guardarLugar(LugarDTO lugarDTO);
    LugarDTO buscarPorId(UUID id);
    void eliminarLugar(UUID id);
    List<LugarDTO> buscarPorNombre(String nombre);
    List<LugarDTO> buscarPorCapacidadMayorA(int capacidad);
}
