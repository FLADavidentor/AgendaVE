package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.Actividad.ActividadInscritaDTO;
import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import com.uam.agendave.dto.Registro.*;
import com.uam.agendave.dto.Notificaciones.MeetingDetailsDTO;
import com.uam.agendave.exception.CupoFullException;
import com.uam.agendave.manager.ZonaAsistenciaManager;
import com.uam.agendave.mapper.ActividadMapper;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.EstudianteRepository;
import com.uam.agendave.repository.RegistroRepository;
import com.uam.agendave.service.email.EmailService;
import com.uam.agendave.service.actividad.ActividadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.uam.agendave.util.GeoUtils.calcularDistanciaEnMetros;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final ZonaAsistenciaManager zonaAsistenciaManager;
    private final RegistroRepository repository;
    private final EstudianteRepository estudianteRepository;
    private final EmailService emailService;
    private final RegistroNotifService registroNotifService;
    private final AsistenciaNotifService asistenciaNotifService;
    private final ActividadMapper actividadMapper;

@Autowired
    public RegistroServiceImpl(RegistroRepository repository,
                               EstudianteRepository estudianteRepository,
                               EmailService emailService,
                               RegistroNotifService registroNotifService,
                               AsistenciaNotifService asistenciaNotifService,
                               ZonaAsistenciaManager zonaAsistenciaManager,
                               ActividadMapper actividadMapper
                               ) {
        this.repository = repository;
        this.estudianteRepository = estudianteRepository;
        this.emailService = emailService;
        this.registroNotifService = registroNotifService;
        this.asistenciaNotifService = asistenciaNotifService;
        this.zonaAsistenciaManager = zonaAsistenciaManager;
        this.actividadMapper = actividadMapper;

    }

    @Override
    @Transactional
    public void guardarRegistro(RegistroDTO registroDTO, ActividadService actividadService) {


        try {
            Actividad actividad = actividadService.buscarPorId(registroDTO.getIdActividad());

            long inscritos = repository.countByActividadId(registroDTO.getIdActividad());

            if (inscritos >= actividad.getCupo()) {
                throw new CupoFullException("Cupo agotado para la actividad " + actividad.getNombreActividad());
            }

            Map<TipoConvalidacion, Integer> variable = actividad.getConvalidacionesPermitidas();

            Registro registro = new Registro();

            registro.setActividad(actividad);
            registro.setEstudiante(RegistroHelper.getEstudianteByCif(registroDTO.getCif()));
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

            MeetingDetailsDTO meeting = new MeetingDetailsDTO();
            meeting.setTitle(registro.getActividad().getNombre());
            meeting.setDate(registro.getActividad().getFecha().toString());
            meeting.setLocation(registro.getActividad().getLugar().getNombre());
            meeting.setSenderName("Equipo de Vida Estudiantil");
            meeting.setPurpose("Informarte que has sido inscrito correctamente en la actividad programada y brindarte los detalles necesarios para asegurar tu participación.");
            meeting.setTime(registro.getActividad().getHoraInicio().toString());
//        meeting.setSenderName("Equipo de Vida Estudiantil");


//        emailService.sendConfirmation(estudiante.get().getCorreo(), subject, body);


            emailService.sendMeetingInvitation(estudiante.get().getCorreo(), subject, meeting);
            registroNotifService.notificarRegistroCreado(registroDTO);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
}


    @Transactional()
    public Map<TipoConvalidacion, Integer> obtenerTotalCreditosPorTipo(String cif) {
        return repository.findByEstudianteCif(cif).stream()
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
                .findByEstudianteCifAndActividadId(asistenciaDTO.getCif(), asistenciaDTO.getIdActividad())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No existe registro para CIF=" + asistenciaDTO.getCif() +
                                        " y actividad=" + asistenciaDTO.getIdActividad()
                        )
                );


        registro.setEstadoAsistencia(asistenciaDTO.getEstadoAsistencia());

        // Actualizar timestamp
        registro.setAsistenciaTimestamp(LocalDateTime.now());

        asistenciaNotifService.notifAsistenciaEstado(asistenciaDTO);
    }


    //Conocer los estudiantes que esten inscritos a una actividad
    @Transactional
    public List<ActividadInscritaDTO> buscarActividadesInscritasPorCif(String cif) {
        return repository.findByEstudianteCif(cif)
                .stream()
                .map(registro -> {
                    ActividadInscritaDTO dto = new ActividadInscritaDTO();
                    dto.setActividad(actividadMapper.toDTO(registro.getActividad()));
                    dto.setTipoConvalidacion(registro.getTipoConvalidacion().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<InscritoResponseDTO> verificarValorDeInscripcion(InscritoBodyDTO inscritoBodyDTO) {
        List<InscritoResponseDTO> result = new ArrayList<>();

        String cif = inscritoBodyDTO.getCif(); // BRO you forgot this line

        for (String actividadIdStr : inscritoBodyDTO.getActividades()) {
            try {
                UUID actividadId = UUID.fromString(actividadIdStr);

                Optional<Registro> registroOpt = repository.findByEstudianteCifAndActividadId(cif, actividadId);
                boolean estaInscrito = registroOpt.isPresent();

                InscritoResponseDTO dto = new InscritoResponseDTO();
                dto.setIdActividad(actividadId.toString());
                dto.setInscrito(estaInscrito);

                result.add(dto);

            } catch (IllegalArgumentException e) {

            }
        }

        return result;
    }
    @Override
    @Transactional
    public ResponseEntity<?> calcularAsistencia(LocationAsistenciaDTO dto) {
        try {
            Optional<ZonaAsistenciaDTO> zonaOpt = zonaAsistenciaManager.obtenerZona(dto.getIdActividad());

            if (zonaOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "No hay zona activa configurada para esta actividad"
                ));
            }

            ZonaAsistenciaDTO zona = zonaOpt.get();

            if (LocalDateTime.now().isAfter(zona.getTiempoLimite())) {
                return ResponseEntity.status(403).body(Map.of(
                        "success", false,
                        "message", "Ya no se puede marcar asistencia, tiempo límite vencido"
                ));
            }

            double distancia = calcularDistanciaEnMetros(
                    zona.getLat(), zona.getLng(),
                    dto.getLatitud(), dto.getLongitud());

            if (distancia > zona.getRadioMetros()) {
                return ResponseEntity.status(403).body(Map.of(
                        "success", false,
                        "message", "Estás fuera del rango permitido para marcar asistencia",
                        "distancia", distancia
                ));
            }

            AsistenciaDTO asistencia = new AsistenciaDTO();
            asistencia.setCif(dto.getCif());
            asistencia.setIdActividad(dto.getIdActividad());
            asistencia.setEstadoAsistencia(EstadoAsistencia.PRESENTE);

            marcarAsistencia(asistencia);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Asistencia marcada correctamente",
                    "distancia", distancia
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error inesperado al calcular la asistencia",
                    "error", e.getMessage()
            ));
        }
    }




}
