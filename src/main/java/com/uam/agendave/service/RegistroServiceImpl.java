package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.EstadoAsistencia;
import com.uam.agendave.model.Registro;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.repository.RegistroRepository;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
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
}
