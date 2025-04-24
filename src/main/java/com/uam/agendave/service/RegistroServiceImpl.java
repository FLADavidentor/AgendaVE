package com.uam.agendave.service;

import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.RegistroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
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
        registro.setTipoConvalidacion(registroDTO.getTipoConvalidacion());

        repository.save(registro);


    }

    @Transactional()
    public Map<TipoConvalidacion, Integer> obtenerTotalCreditosPorTipo(String cif) {
        return repository.findByCif(cif).stream()
                // filtramos sólo los registros con asistencia presente
                .filter(r -> r.getEstadoAsistencia() == EstadoAsistencia.PRESENTE)
                // agrupamos y sumamos
                .collect(Collectors.groupingBy(
                        Registro::getTipoConvalidacion,
                        () -> new EnumMap<>(TipoConvalidacion.class),
                        Collectors.summingInt(Registro::getTotalConvalidado)
                ));
    }

    @Override
    @Transactional
    public void marcarAsistencia(AsistenciaDTO asistenciaDTO) {
        Registro registro = repository
                .findByCifAndActividadId(asistenciaDTO.getCif(), asistenciaDTO.getIdActividad())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No existe registro para CIF=" + asistenciaDTO.getCif() +
                                        " y actividad=" + asistenciaDTO.getIdActividad()
                        )
                );

        // ✅ Aplicar el valor real recibido
        registro.setEstadoAsistencia(asistenciaDTO.getEstadoAsistencia());

        // Actualizar timestamp
        registro.setAsistenciaTimestamp(LocalDateTime.now());
    }


    @Transactional
    public List<Actividad> buscarActividadesInscritasPorCif(String cif) {

        List<Actividad> registrosDelEstudiante = repository.findByCif(cif).stream().map(Registro::getActividad).collect(Collectors.toList());
        System.out.println(registrosDelEstudiante);
        return registrosDelEstudiante;

    }

    //Conocer los estudiantes que esten inscritos a una actividad

}
