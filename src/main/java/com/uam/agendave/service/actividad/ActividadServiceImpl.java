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
import com.uam.agendave.service.imagen.ImageStorageService;
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
    private final ImageStorageService imageStorageService;

    @Autowired
    public ActividadServiceImpl(ActividadRepository actividadRepository,
                                NombreActividadRepository nombreActividadRepository,
                                LugarRepository lugarRepository,
                                RegistroRepository registroRepository,
                                EstudianteRepository estudianteRepository,
                                ActividadNotifService actividadNotifService,
                                TransporteService transporteService,
                                ImageStorageService imageStorageService) {
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
        this.imageStorageService = imageStorageService;
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
    public List<ActividadDTO> obtenerActividadesActivas(boolean incluirCuposRestantes) {
        List<Actividad> actividades = actividadRepository.findByEstado(true);
        return actividades.stream()
                .map(a -> toDTOConCupos(a, incluirCuposRestantes))
                .collect(toList());
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

            if (actividadDTO.getIdTransporte() != null) {
                Transporte transporte = transporteService.buscarEntidadPorId(actividadDTO.getIdTransporte());
                actividad.setTransporte(transporte);
            }

            actividadRepository.save(actividad);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private Actividad getActividad(ActividadDTO actividadDTO, NombreActividad nombreActividad, Lugar lugar) {
        Actividad actividad = new Actividad();

        ImagenDTO imgDto = actividadDTO.getImagen();
        if (imgDto != null && imgDto.getImagenBase64() != null && !imgDto.getImagenBase64().isBlank()) {
            try {
                String filename = imageStorageService.saveImage(imgDto.getImagenBase64());
                actividad.setImagenPath(filename);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
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

        // ✅ image replace logic
        String oldFilename = actividadExistente.getImagenPath();
        ImagenDTO imgDto = actividadDTO.getImagen();
        if (imgDto != null && imgDto.getImagenBase64() != null && !imgDto.getImagenBase64().isBlank()) {
            try {
                String newFilename = imageStorageService.saveImage(imgDto.getImagenBase64());
                actividadExistente.setImagenPath(newFilename);

                if (oldFilename != null && !oldFilename.equals(newFilename)) {
                    Long usos = actividadRepository.countByImagenPath(oldFilename);
                    imageStorageService.deleteImageIfUnused(oldFilename, usos);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la nueva imagen", e);
            }
        }

        actividadExistente.setDescripcion(actividadDTO.getDescripcion());
        actividadExistente.setFecha(actividadDTO.getFecha());
        actividadExistente.setHoraInicio(actividadDTO.getHoraInicio());
        actividadExistente.setHoraFin(actividadDTO.getHoraFin());
        actividadExistente.setEstado(actividadDTO.isEstado());
        actividadExistente.setCupo(actividadDTO.getCupo());
        actividadExistente.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
        actividadExistente.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());

        NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    NombreActividad nueva = new NombreActividad();
                    nueva.setNombre(actividadDTO.getNombreActividad());
                    return nombreActividadService.guardar(nueva);
                });
        actividadExistente.setNombreActividad(nombreActividad);

        Lugar lugar = lugarRepository.findByNombre(actividadDTO.getLugar())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lugar no encontrado: " + actividadDTO.getLugar()));
        actividadExistente.setLugar(lugar);

        if (actividadDTO.getIdTransporte() != null) {
            Transporte transporte = transporteService.buscarEntidadPorId(actividadDTO.getIdTransporte());
            actividadExistente.setTransporte(transporte);
        } else {
            actividadExistente.setTransporte(null);
        }

        Actividad actividadActualizada = actividadRepository.save(actividadExistente);

        if (actividadDTO.isEstado()) {
            actividadNotifService.notificarActividadPublicada(actividadMapper.toDTO(actividadActualizada));
        } else {
            actividadNotifService.notificarActividadDesPublicada(actividadMapper.toDTO(actividadActualizada));
        }

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
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada con ID: " + id));

        String filename = actividad.getImagenPath();

        registroRepository.deleteByActividadId(id);
        actividadRepository.deleteById(id);

        if (filename != null) {
            long usos = actividadRepository.countByImagenPath(filename);
            imageStorageService.deleteImageIfUnused(filename, usos);
        }
    }

    private ActividadDTO toDTOConCupos(Actividad actividad, boolean incluirCuposRestantes) {
        ActividadDTO dto = actividadMapper.toDTO(actividad);

        if (incluirCuposRestantes) {
            long inscritos = registroRepository.countByActividadId(actividad.getId());
            dto.setCuposRestantes(actividad.getCupo() - (int) inscritos);
        }

        return dto;
    }





}
