package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadInscritaDTO;
import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.EstudianteRepository;
import com.uam.agendave.repository.RegistroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository repository;
    private final EstudianteRepository estudianteRepository;
    private final EmailService emailService;


    public RegistroServiceImpl(RegistroRepository repository, EstudianteRepository estudianteRepository, EmailService emailService) {
        this.repository = repository;
        this.estudianteRepository = estudianteRepository;
        this.emailService = emailService;

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

        Optional<Estudiante> estudiante = estudianteRepository.findByCif(registroDTO.getCif());
        String subject = "Confirmación de inscripción a la actividad";
        String body = String.format("Hola %s, te has inscrito correctamente en la actividad:%n%s a las %s.",
                estudiante.get().getNombres(),
                registro.getActividad().getNombre(),
                registro.getActividad().getFecha());
        emailService.sendConfirmation(estudiante.get().getCorreo(), subject, body);
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


    //Conocer los estudiantes que esten inscritos a una actividad
    @Transactional
    public List<ActividadInscritaDTO> buscarActividadesInscritasPorCif(String cif) {
        return repository.findByCif(cif)
                .stream()
                .map(registro -> {
                    ActividadInscritaDTO dto = new ActividadInscritaDTO();
                    dto.setActividad(registro.getActividad());
                    dto.setTipoConvalidacion(registro.getTipoConvalidacion().name()); // suponiendo que TipoConvalidacion es Enum
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
