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
        NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NombreActividad no encontrado: " + actividadDTO.getNombreActividad()));

        Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));

        TipoActividad tipoActividad = tipoActividadRepository.findByNombreTipoContainingIgnoreCase(actividadDTO.getTipoActividad())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("TipoActividad no encontrado: " + actividadDTO.getTipoActividad()));

        // Crear entidad Actividad
        Actividad actividad = new Actividad();
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFecha(actividadDTO.getFecha());
        actividad.setHoraInicio(actividadDTO.getHoraInicio());
        actividad.setHoraFin(actividadDTO.getHoraFin());
        actividad.setCupo(actividadDTO.getCupo());
        actividad.setNombreActividad(nombreActividad);
        actividad.setLugar(lugar);
        actividad.setTipoActividad(tipoActividad);

        // Guardar en la base de datos
        Actividad actividadGuardada = actividadRepository.save(actividad);

        // Convertir la entidad guardada a DTO y devolver
        return convertirAModelDTO(actividadGuardada);
    }



    @Override
    public Actividad buscarPorId(UUID id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
    }

    @Override
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        Actividad actividadExistente = actividadRepository.findById(actividadDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("La actividad a actualizar no existe con ID: " + actividadDTO.getId()));

        // Actualizar los campos simples
        actividadExistente.setDescripcion(actividadDTO.getDescripcion());
        actividadExistente.setFecha(actividadDTO.getFecha());
        actividadExistente.setHoraInicio(actividadDTO.getHoraInicio());
        actividadExistente.setHoraFin(actividadDTO.getHoraFin());
        actividadExistente.setCupo(actividadDTO.getCupo());

        // Buscar y asignar las relaciones basadas en nombres
        NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NombreActividad no encontrado: " + actividadDTO.getNombreActividad()));
        actividadExistente.setNombreActividad(nombreActividad);

        Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
        actividadExistente.setLugar(lugar);

        TipoActividad tipoActividad = tipoActividadRepository.findByNombreTipoContainingIgnoreCase(actividadDTO.getTipoActividad())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("TipoActividad no encontrado: " + actividadDTO.getTipoActividad()));
        actividadExistente.setTipoActividad(tipoActividad);

        // Guardar los cambios
        Actividad actividadActualizada = actividadRepository.save(actividadExistente);

        return convertirAModelDTO(actividadActualizada);
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

        // Convertir relaciones a nombres
        actividadDTO.setNombreActividad(actividad.getNombreActividad() != null ? actividad.getNombreActividad().getNombre() : null);
        actividadDTO.setLugar(actividad.getLugar() != null ? actividad.getLugar().getNombre() : null);
        actividadDTO.setTipoActividad(actividad.getTipoActividad() != null ? actividad.getTipoActividad().getNombreTipo() : null);

        // Convalidaciones
        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;
    }

}
