package com.uam.agendave.service.nombreActividad;

import com.uam.agendave.model.NombreActividad;
import com.uam.agendave.repository.NombreActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NombreActividadServiceImpl implements NombreActividadService {

    private final NombreActividadRepository nombreActividadRepository;

    public NombreActividadServiceImpl(NombreActividadRepository nombreActividadRepository) {
        this.nombreActividadRepository = nombreActividadRepository;
    }

    @Override
    public List<NombreActividad> buscarPorNombre(String nombre) {
        return nombreActividadRepository.findByNombre(nombre);
    }

    @Override
    public List<NombreActividad> buscarPorNombreParcial(String parteDelNombre) {
        return nombreActividadRepository.findByNombreContainingIgnoreCase(parteDelNombre);
    }

    @Override
    public NombreActividad guardar(NombreActividad nombreActividad) {
        return nombreActividadRepository.save(nombreActividad);
    }
}
