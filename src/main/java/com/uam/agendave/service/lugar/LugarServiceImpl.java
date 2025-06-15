package com.uam.agendave.service.lugar;

import com.uam.agendave.dto.LugarDTO;
import com.uam.agendave.mapper.LugarMapper;
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
    private final LugarMapper lugarMapper;

    @Autowired
    public LugarServiceImpl(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
        this.lugarMapper = new LugarMapper();
    }

    @Override
    public List<LugarDTO> obtenerTodos() {
        return lugarRepository.findAll().stream()
                .map(lugarMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LugarDTO guardarLugar(LugarDTO lugarDTO) {
        Lugar lugar = LugarMapper.toEntity(lugarDTO);
        Lugar lugarGuardado = lugarRepository.save(lugar);
        return lugarMapper.toDTO(lugarGuardado);
    }

    @Override
    public LugarDTO buscarPorId(UUID id) {
        Lugar lugar = lugarRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Lugar no encontrado con ID: " + id));
        return lugarMapper.toDTO(lugar);
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
                .map(lugarMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<LugarDTO> buscarPorCapacidadMayorA(int capacidad) {
        return lugarRepository.findByCapacidadGreaterThan(capacidad).stream()
                .map(lugarMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LugarDTO> buscarPorNombreParcial(String nombre) {
        return lugarRepository.findByNombreStartingWith(nombre).stream()
                .map(lugarMapper::toDTO)
                .collect(Collectors.toList());
    }

}
