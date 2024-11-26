package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final NombreActividadRepository nombreActividadRepository;
    private final LugarRepository lugarRepository;
    private final TipoActividadRepository tipoActividadRepository;
    private final UsuarioRepository usuarioRepository;

    public ActividadServiceImpl(
            ActividadRepository actividadRepository,
            NombreActividadRepository nombreActividadRepository,
            LugarRepository lugarRepository,
            TipoActividadRepository tipoActividadRepository,
            UsuarioRepository usuarioRepository) {
        this.actividadRepository = actividadRepository;
        this.nombreActividadRepository = nombreActividadRepository;
        this.lugarRepository = lugarRepository;
        this.tipoActividadRepository = tipoActividadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<ActividadDTO> obtenerTodas() {
        List<Actividad> actividades = actividadRepository.findAll();
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadDTO guardarActividad(ActividadDTO actividadDTO) {
        // Validar y buscar entidades relacionadas
        NombreActividad nombreActividad = nombreActividadRepository.findById(actividadDTO.getIdNombreActividad())
                .orElseThrow(() -> new IllegalArgumentException("NombreActividad no encontrado con ID: " + actividadDTO.getIdNombreActividad()));

        Lugar lugar = lugarRepository.findById(actividadDTO.getIdLugar())
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado con ID: " + actividadDTO.getIdLugar()));

        TipoActividad tipoActividad = tipoActividadRepository.findById(actividadDTO.getIdTipoActividad())
                .orElseThrow(() -> new IllegalArgumentException("TipoActividad no encontrado con ID: " + actividadDTO.getIdTipoActividad()));

        Usuario usuario = usuarioRepository.findById(actividadDTO.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + actividadDTO.getIdUsuario()));

        // Crear entidad Actividad
        Actividad actividad = convertirAEntidad(actividadDTO, nombreActividad, lugar, tipoActividad, usuario);

        // Calcular cupo si no está definido
        if (actividad.getCupo() == 0) {
            actividad.setCupo(lugar.getCapacidad());
        }

        Actividad actividadGuardada = actividadRepository.save(actividad);

        return convertirAModelDTO(actividadGuardada);
    }

    @Override
    public Actividad buscarPorId(UUID id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
    }

    @Override
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        if (!actividadRepository.existsById(actividadDTO.getId())) {
            throw new IllegalArgumentException("La actividad a actualizar no existe con ID: " + actividadDTO.getId());
        }
        return guardarActividad(actividadDTO); // Reutiliza la lógica de guardar para la actualización
    }

    @Override
    public void eliminarActividad(UUID id) {
        if (!actividadRepository.existsById(id)) {
            throw new IllegalArgumentException("Actividad no encontrada con ID: " + id);
        }
        actividadRepository.deleteById(id);
    }

    @Override
    public List<ActividadDTO> buscarPorNombre(String nombre) {
        List<Actividad> actividades = actividadRepository.findByNombreActividadNombre(nombre);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadDTO> buscarPorLugar(UUID idLugar) {
        List<Actividad> actividades = actividadRepository.findByLugarId(idLugar);
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadDTO> buscarActividadesConCupoDisponible() {
        List<Actividad> actividades = actividadRepository.findActividadesConCupoDisponible();
        return actividades.stream()
                .map(this::convertirAModelDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Map<TipoConvalidacion, Integer> obtenerConvalidacionesPorActividad(UUID id) {
        List<Object[]> resultados = actividadRepository.findConvalidacionesById(id);

        // Convertir la lista en un mapa
        return resultados.stream()
                .collect(Collectors.toMap(
                        resultado -> (TipoConvalidacion) resultado[0], // Clave (TipoConvalidacion)
                        resultado -> (Integer) resultado[1]           // Valor (cantidadPermitida)
                ));
    }


    @Override
    public Integer obtenerTotalConvalidacionesMaximas(UUID id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
        return actividad.getTotalConvalidacionesPermitidas();
    }



    // Métodos de conversión

    private Actividad convertirAEntidad(ActividadDTO actividadDTO, NombreActividad nombreActividad, Lugar lugar, TipoActividad tipoActividad, Usuario usuario) {
        Actividad actividad = new Actividad();
        actividad.setId(actividadDTO.getId());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFecha(actividadDTO.getFecha());
        actividad.setHoraInicio(actividadDTO.getHoraInicio());
        actividad.setHoraFin(actividadDTO.getHoraFin());
        actividad.setEstado(actividadDTO.isEstado());
        actividad.setCupo(actividadDTO.getCupo());
        actividad.setNombre(nombreActividad.getNombre());
        actividad.setNombreActividad(nombreActividad);
        actividad.setLugar(lugar);
        actividad.setTipoActividad(tipoActividad);
        actividad.setUsuario(usuario);
        actividad.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());
        return actividad;
    }

    private ActividadDTO convertirAModelDTO(Actividad actividad) {
        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setId(actividad.getId());
        actividadDTO.setDescripcion(actividad.getDescripcion());
        actividadDTO.setFecha(actividad.getFecha());
        actividadDTO.setHoraInicio(actividad.getHoraInicio());
        actividadDTO.setHoraFin(actividad.getHoraFin());
        actividadDTO.setEstado(actividad.isEstado());
        actividadDTO.setCupo(actividad.getCupo());
        actividadDTO.setIdNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getId() : null);
        actividadDTO.setIdLugar(actividad.getLugar() != null ? actividad.getLugar().getId() : null);
        actividadDTO.setIdTipoActividad(actividad.getTipoActividad() != null ? actividad.getTipoActividad().getId() : null);
        actividadDTO.setIdUsuario(actividad.getUsuario() != null ? actividad.getUsuario().getId() : null);
        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());
        return actividadDTO;
    }
}
