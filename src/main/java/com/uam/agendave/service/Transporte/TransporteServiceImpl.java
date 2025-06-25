package com.uam.agendave.service.Transporte;

import com.uam.agendave.dto.Actividad.TransporteDTO;
import com.uam.agendave.mapper.TransporteMapper;
import com.uam.agendave.model.Lugar;
import com.uam.agendave.model.Transporte;
import com.uam.agendave.repository.LugarRepository;
import com.uam.agendave.repository.TransporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransporteServiceImpl implements TransporteService {

    private final TransporteRepository transporteRepository;
    private final LugarRepository lugarRepository;
    private final TransporteMapper transporteMapper;

    private Lugar obtenerLugarExistente(String nombreLugar) {
        return lugarRepository.findByNombre(nombreLugar)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + nombreLugar));
    }

    @Autowired
    public TransporteServiceImpl(TransporteRepository transporteRepository, LugarRepository lugarRepository) {
        this.transporteRepository = transporteRepository;
        this.lugarRepository = lugarRepository;
        this.transporteMapper = new TransporteMapper();
    }

    @Override
    public List<TransporteDTO> obtenerTodos() {
        return transporteRepository.findAll().stream()
                .map(transporteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransporteDTO guardar(TransporteDTO transporteDTO) {

        Transporte transporte = new Transporte();
        transporte.setLugar(obtenerLugarExistente(transporteDTO.getLugar()));
        transporte.setCapacidad(transporteDTO.getCapacidad());

        Transporte guardado = transporteRepository.save(transporte);
        return transporteMapper.toDTO(guardado);
    }

    @Override
    public Transporte guardarSiNoExiste(TransporteDTO transporteDTO) {
        Lugar lugar = obtenerLugarExistente(transporteDTO.getLugar());
        return transporteRepository
                .findByLugarNombreIgnoreCaseAndCapacidad(lugar.getNombre(), transporteDTO.getCapacidad())
                .orElseGet(() -> {
                    Transporte nuevo = transporteMapper.toEntity(transporteDTO, lugar);
                    return transporteRepository.save(nuevo);
                });
    }


    @Override
    public TransporteDTO buscarPorId(UUID id) {
        Transporte transporte = transporteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado con ID: " + id));
        return transporteMapper.toDTO(transporte);
    }

    @Override
    public void eliminar(UUID id) {
        if (!transporteRepository.existsById(id)) {
            throw new IllegalArgumentException("Transporte no encontrado con ID: " + id);
        }
        transporteRepository.deleteById(id);
    }

    @Override
    public List<TransporteDTO> buscarPorLugar(String nombreLugar) {
        return transporteRepository.findByLugarNombre(nombreLugar).stream()
                .map(transporteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Transporte buscarEntidadPorId(UUID id) {
        return transporteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado con ID: " + id));
    }

}
