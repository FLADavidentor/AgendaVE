package com.uam.agendave.service;

import com.uam.agendave.dto.LugarDTO;
import com.uam.agendave.model.Lugar;
import com.uam.agendave.repository.LugarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LugarServiceImpl implements LugarService {

    private final LugarRepository lugarRepository;

    @Autowired
    public LugarServiceImpl(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    @Override
    public List<LugarDTO> obtenerTodos() {
        return lugarRepository.findAll().stream()
                .map(this::convertirA_dto)
                .collect(Collectors.toList());
    }

    @Override
    public LugarDTO guardarLugar(LugarDTO lugarDTO) {
        Lugar lugar = convertirAEntidad(lugarDTO);
        Lugar lugarGuardado = lugarRepository.save(lugar);
        return convertirA_dto(lugarGuardado);
    }

    @Override
    public LugarDTO buscarPorId(UUID id) {
        Lugar lugar = lugarRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Lugar no encontrado con ID: " + id));
        return convertirA_dto(lugar);
    }

    @Override
    public void eliminarLugar(UUID id) {
        if (!lugarRepository.existsById(id)) {
            throw new IllegalArgumentException("Lugar no encontrado con ID: " + id);
        }
        lugarRepository.deleteById(id);
    }

    @Override
    public List<LugarDTO> buscarPorNombre(String nombre) {
        return lugarRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirA_dto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LugarDTO> buscarPorCapacidadMayorA(int capacidad) {
        return lugarRepository.findByCapacidadGreaterThan(capacidad).stream()
                .map(this::convertirA_dto)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares para convertir entre entidades y DTOs
    private LugarDTO convertirA_dto(Lugar lugar) {
        LugarDTO lugarDTO = new LugarDTO();
        lugarDTO.setId(lugar.getId());
        lugarDTO.setNombre(lugar.getNombre());
        lugarDTO.setCapacidad(lugar.getCapacidad());
        lugarDTO.setUbicacion(lugar.getUbicacion());
        return lugarDTO;
    }

    private Lugar convertirAEntidad(LugarDTO lugarDTO) {
        Lugar lugar = new Lugar();
        lugar.setId(lugarDTO.getId());
        lugar.setNombre(lugarDTO.getNombre());
        lugar.setCapacidad(lugarDTO.getCapacidad());
        lugar.setUbicacion(lugarDTO.getUbicacion());
        return lugar;
    }
}
