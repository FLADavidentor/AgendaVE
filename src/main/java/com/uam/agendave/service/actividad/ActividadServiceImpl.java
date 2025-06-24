package com.uam.agendave.service.actividad;

import com.uam.agendave.dto.Actividad.ActividadDTO;
import com.uam.agendave.dto.Actividad.TransporteDTO;
import com.uam.agendave.dto.Usuario.EstudianteDTO;
import com.uam.agendave.dto.Actividad.ImagenDTO;
import com.uam.agendave.exception.NotFoundException;
import com.uam.agendave.mapper.EstudianteMapper;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.*;
import com.uam.agendave.service.Transporte.TransporteService;
import com.uam.agendave.service.nombreActividad.NombreActividadService;
import com.uam.agendave.service.nombreActividad.NombreActividadServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import com.uam.agendave.mapper.ActividadMapper;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final NombreActividadRepository nombreActividadRepository;
    private final NombreActividadService nombreActividadService;
    private final LugarRepository lugarRepository;
    private final RegistroRepository registroRepository;
    private final EstudianteRepository estudianteRepository;
    private final ActividadMapper actividadMapper;
    private final EstudianteMapper estudianteMapper;
    private final ActividadNotifService actividadNotifService;
    private final TransporteService transporteService;

    @Autowired
    public ActividadServiceImpl(ActividadRepository actividadRepository, NombreActividadRepository nombreActividadRepository, LugarRepository lugarRepository, RegistroRepository registroRepository, EstudianteRepository estudianteRepository,ActividadNotifService actividadNotifService,TransporteService transporteService) {
        this.actividadRepository = actividadRepository;
        this.nombreActividadRepository = nombreActividadRepository;
        this.nombreActividadService = new NombreActividadServiceImpl(nombreActividadRepository);
        this.lugarRepository = lugarRepository;
        this.registroRepository = registroRepository;
        this.estudianteRepository = estudianteRepository;
        this.actividadMapper = new ActividadMapper();
        this.estudianteMapper = new EstudianteMapper();
        this.actividadNotifService = actividadNotifService;
        this.transporteService = transporteService;
    }

    @Override
    @Transactional
    public List<ActividadDTO> obtenerTodas() {
        // Obtener todas las actividades desde el repositorio

        List<Actividad> actividades = actividadRepository.findAll();

        // Convertir las entidades a DTOs
        return actividades.stream().map(actividadMapper::toDTO).collect(toList());
    }

    @Override
    @Transactional
    public List<ActividadDTO> obtenerActividadesActivas() {
        // Obtener todas las actividades desde el repositorio

        List<Actividad> actividades = actividadRepository.findByEstado(true);

        // Convertir las entidades a DTOs
        return actividades.stream().map(actividadMapper::toDTO).collect(toList());
    }

    @Override
    @Transactional
    public List<ActividadDTO> obtenerActividadesPorNombre(String nombre) {
        try {
            List<Actividad> actividades = actividadRepository.findByNombre(nombre);

            return actividades.stream().map(actividadMapper::toDTO).collect(toList());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void guardarActividad(ActividadDTO actividadDTO) throws Exception {
        try {
            // Buscar o crear NombreActividad
            NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        NombreActividad nueva = new NombreActividad();
                        nueva.setNombre(actividadDTO.getNombreActividad());
                        return nombreActividadService.guardar(nueva);
                    });

            Lugar lugar = lugarRepository.findByNombre(actividadDTO.getLugar())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Lugar no encontrado: " + actividadDTO.getLugar()));

            Actividad actividad = getActividad(actividadDTO, nombreActividad, lugar);

            // Asociar transporte si se proporcionó un ID
            if (actividadDTO.getIdTransporte() != null) {
                Transporte transporte = transporteService.buscarEntidadPorId(actividadDTO.getIdTransporte()); // ya existe
                actividad.setTransporte(transporte);
            }

            actividadRepository.save(actividad);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static Actividad getActividad(ActividadDTO actividadDTO, NombreActividad nombreActividad, Lugar lugar) {
        Actividad actividad = new Actividad();

        ImagenDTO imgDto = actividadDTO.getImagen();
        if (imgDto != null && imgDto.getImagenBase64() != null && !imgDto.getImagenBase64().trim().isEmpty()) {

            ImageData imageData = new ImageData();
            imageData.setNombre(imgDto.getNombre());
            imageData.setImagenBase64(imgDto.getImagenBase64());
            System.out.println(imageData.getImagenBase64());
            actividad.setImagen(imageData);
            System.out.println(actividad.getImagen().getImagenBase64());

        } else {
            // Opcional: asegurarte de que la entidad actividad permita imagen null
            actividad.setImagen(null);
        }

        actividad.setNombre(nombreActividad.getNombre());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFecha(actividadDTO.getFecha());
        actividad.setHoraInicio(actividadDTO.getHoraInicio());
        actividad.setHoraFin(actividadDTO.getHoraFin());
        actividad.setCupo(actividadDTO.getCupo());
        actividad.setNombreActividad(nombreActividad);
        actividad.setLugar(lugar);

        actividad.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
        actividad.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());

        return actividad;
    }

    @Override
    public int getCupoRestante(UUID actividadID) {
        Actividad act = actividadRepository.findById(actividadID).orElseThrow(() -> new NotFoundException("Actividad no encontrada con ID: " + actividadID));
        long inscritos = registroRepository.countByActividadId(actividadID);
        return act.getCupo() - (int) inscritos;
    }


    @Override
    @Transactional
    public List<EstudianteDTO> obtenerListadoEstudiante(UUID idActividad) {

        List<Registro> response = registroRepository.findByActividadId(idActividad);
        List<String> cifsInvolucrados = response.stream().map(r -> r.getEstudiante().getCif())
                .toList();
        Optional<List<Estudiante>> estudiantesPorActividad = estudianteRepository.findByCifIn(cifsInvolucrados);

        return estudiantesPorActividad.get().stream().map(estudianteMapper::toDTO).collect(toList());

    }


    @Override
    public Page<ActividadDTO> obtenerActividades(Pageable pageable) {
        return actividadRepository.findAll(pageable).map(actividadMapper::toDTO);
    }



    @Override
    public Actividad buscarPorId(UUID id) {
        return actividadRepository.findById(id).orElseThrow(() -> new NotFoundException("Actividad no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        Actividad actividadExistente = actividadRepository.findById(actividadDTO.getId())
                .orElseThrow(() -> new NotFoundException("La actividad a actualizar no existe con ID: " + actividadDTO.getId()));

        // Actualizar campos simples
        actividadExistente.setDescripcion(actividadDTO.getDescripcion());
        actividadExistente.setFecha(actividadDTO.getFecha());
        actividadExistente.setHoraInicio(actividadDTO.getHoraInicio());
        actividadExistente.setHoraFin(actividadDTO.getHoraFin());
        actividadExistente.setEstado(actividadDTO.isEstado());
        actividadExistente.setCupo(actividadDTO.getCupo());

        // Buscar o crear NombreActividad
        NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    NombreActividad nueva = new NombreActividad();
                    nueva.setNombre(actividadDTO.getNombreActividad());
                    return nombreActividadService.guardar(nueva);
                });
        actividadExistente.setNombreActividad(nombreActividad);

        // Buscar Lugar
        Lugar lugar = lugarRepository.findByNombre(actividadDTO.getLugar())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lugar no encontrado: " + actividadDTO.getLugar()));
        actividadExistente.setLugar(lugar);

        // Asociar transporte si se especificó
        if (actividadDTO.getIdTransporte() != null) {
            Transporte transporte = transporteService.buscarEntidadPorId(actividadDTO.getIdTransporte());
            actividadExistente.setTransporte(transporte);
        } else {
            actividadExistente.setTransporte(null); // Omitir esta línea si no se desea borrar transporte al editar
        }

        // Guardar cambios
        Actividad actividadActualizada = actividadRepository.save(actividadExistente);

        // Notificación WebSocket
        if (actividadDTO.isEstado()) {
            actividadNotifService.notificarActividadPublicada(actividadMapper.toDTO(actividadActualizada));
        } else {
            actividadNotifService.notificarActividadDesPublicada(actividadMapper.toDTO(actividadActualizada));
        }

        // Limpiar NombreActividad si no se usa
        eliminarNombreActividadNoUsado(nombreActividad);

        return actividadMapper.toDTO(actividadActualizada);
    }


    private void eliminarNombreActividadNoUsado(NombreActividad nombreActividad) {
        // Verificar si el NombreActividad está siendo usado por otras actividades
        List<Actividad> actividadesConNombre = actividadRepository.findByNombreActividadId(nombreActividad.getId());
        if (actividadesConNombre.isEmpty()) {
            nombreActividadRepository.delete(nombreActividad); // Eliminarlo si no se usa
        }
    }


    @Override
    @Transactional
    public void eliminarActividad(UUID id) {


        if (!actividadRepository.existsById(id)) {
            throw new NotFoundException("Actividad no encontrada con ID: " + id);
        }
        registroRepository.deleteByActividadId(id);
        actividadRepository.deleteById(id);

    }


}
