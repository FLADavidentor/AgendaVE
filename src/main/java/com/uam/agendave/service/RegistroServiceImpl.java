package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.RegistroRepository;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository repository;

    public RegistroServiceImpl(RegistroRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService) {


        Actividad actividad = actividadService.buscarPorId(registroDTO.getIdActividad());

        long inscritos = repository.countByActividadId(registroDTO.getIdActividad());

        if (inscritos >= actividad.getCupo()) {
            throw new IllegalStateException("Cupo agotado para la actividad " + actividad.getNombreActividad());
        }

        Map<TipoConvalidacion, Integer> variable = actividad.getConvalidacionesPermitidas();

        Registro registro = new Registro();

        registro.setActividad(actividad);
        registro.setCif(registroDTO.getCif());
        registro.setTransporte(registroDTO.getTransporte());
        registro.setTotalConvalidado(variable.values().iterator().next());
        registro.setEstadoAsistencia(EstadoAsistencia.AUSENTE);
        registro.setAsistenciaTimestamp(LocalDateTime.now());
        repository.save(registro);



    }

    //Saber a que actividades esta inscritas un estudiante
    public List<Actividad> buscarActividadesInscritasPorCif(String cif) {
        List <Registro> registrosDelEstudiante = repository.findByCif(cif);
        return registrosDelEstudiante.stream().map(r -> r.getActividad()).collect(Collectors.toList());
    }

    //Conocer los estudiantes que esten inscritos a una actividad
    //TODO : Es necsario tener un registro de los estudiante por su CIF.
}
