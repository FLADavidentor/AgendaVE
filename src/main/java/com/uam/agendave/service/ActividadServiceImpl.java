package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.ImagenDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final NombreActividadRepository nombreActividadRepository;
    private final NombreActividadService nombreActividadService;
    private final LugarRepository lugarRepository;
    private final RegistroRepository registroRepository;
    private final EstudianteRepository estudianteRepository;

    public ActividadServiceImpl(ActividadRepository actividadRepository, NombreActividadRepository nombreActividadRepository, LugarRepository lugarRepository, RegistroRepository registroRepository, EstudianteRepository estudianteRepository) {
        this.actividadRepository = actividadRepository;
        this.nombreActividadRepository = nombreActividadRepository;
        this.nombreActividadService = new NombreActividadServiceImpl(nombreActividadRepository);
        this.lugarRepository = lugarRepository;
        this.registroRepository = registroRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    @Transactional
    public List<ActividadDTO> obtenerTodas() {
        // Obtener todas las actividades desde el repositorio

        List<Actividad> actividades = actividadRepository.findAll();

        // Convertir las entidades a DTOs
        return actividades.stream().map(this::convertirAModelDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ActividadDTO> obtenerActividadesActivas() {
        // Obtener todas las actividades desde el repositorio

        List<Actividad> actividades = actividadRepository.findByEstado(true);

        // Convertir las entidades a DTOs
        return actividades.stream().map(this::convertirAModelDTO).collect(Collectors.toList());
    }

    @Override
    public void guardarActividad(ActividadDTO actividadDTO) throws Exception {

        try {
            // Buscar o crear NombreActividad
            NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad()).stream().findFirst().orElseGet(() -> {
                NombreActividad nuevaNombreActividad = new NombreActividad();
                nuevaNombreActividad.setNombre(actividadDTO.getNombreActividad());
                return nombreActividadService.guardar(nuevaNombreActividad); // Usar el servicio para persistir
            });
            System.out.println(nombreActividad.getId());

            Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar()).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
            System.out.println(lugar.getId());

            Actividad actividad = getActividad(actividadDTO, nombreActividad, lugar);

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
        Actividad act = actividadRepository.findById(actividadID).orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + actividadID));
        long inscritos = registroRepository.countByActividadId(actividadID);
        return act.getCupo() - (int) inscritos;
    }


    @Override
    @Transactional
    public List<EstudianteDTO> obtenerListadoEstudiante(UUID idActividad) {

        List<Registro> response = registroRepository.findByActividadId(idActividad);
        List<String> cifsInvolucrados = response.stream().map(Registro::getCif).toList();
        Optional<List<Estudiante>> estudiantesPorActividad = estudianteRepository.findByCifIn(cifsInvolucrados);

        return estudiantesPorActividad.get().stream().map(this::mapearEstudianteDTO).collect(Collectors.toList());

    }


    @Override
    public Page<ActividadDTO> obtenerActividades(Pageable pageable) {
        return actividadRepository.findAll(pageable).map(this::convertirAModelDTO);
    }



    @Override
    public Actividad buscarPorId(UUID id) {
        return actividadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
    }

    @Override
    public ActividadDTO actualizarActividad(ActividadDTO actividadDTO) {
        Actividad actividadExistente = actividadRepository.findById(actividadDTO.getId()).orElseThrow(() -> new IllegalArgumentException("La actividad a actualizar no existe con ID: " + actividadDTO.getId()));

        // Actualizar los campos simples
        actividadExistente.setDescripcion(actividadDTO.getDescripcion());
        actividadExistente.setFecha(actividadDTO.getFecha());
        actividadExistente.setHoraInicio(actividadDTO.getHoraInicio());
        actividadExistente.setHoraFin(actividadDTO.getHoraFin());
        actividadExistente.setEstado(actividadDTO.isEstado());
        actividadExistente.setCupo(actividadDTO.getCupo());

        // Buscar o crear NombreActividad
        NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad()).stream().findFirst().orElseGet(() -> {
            NombreActividad nuevaNombreActividad = new NombreActividad();
            nuevaNombreActividad.setNombre(actividadDTO.getNombreActividad());
            return nombreActividadService.guardar(nuevaNombreActividad); // Usar el servicio para persistir
        });

        actividadExistente.setNombreActividad(nombreActividad);

        Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar()).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
        actividadExistente.setLugar(lugar);

        // Guardar los cambios
        Actividad actividadActualizada = actividadRepository.save(actividadExistente);

        // Eliminar NombreActividad si ya no está siendo usado
        eliminarNombreActividadNoUsado(nombreActividad);

        return convertirAModelDTO(actividadActualizada);
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
            throw new IllegalArgumentException("Actividad no encontrada con ID: " + id);
        }
        registroRepository.deleteByActividadId(id);
        actividadRepository.deleteById(id);

    }


    private ActividadDTO convertirAModelDTO(Actividad actividad) {
        ActividadDTO actividadDTO = new ActividadDTO();
        ImagenDTO imagenDTO = new ImagenDTO();
        imagenDTO.setNombre(actividad.getImagen().getNombre());
        imagenDTO.setImagenBase64(actividad.getImagen().getImagenBase64());

        actividadDTO.setImagen(imagenDTO);
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

        // Convalidaciones
        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;


    }

    private EstudianteDTO mapearEstudianteDTO(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setCif(e.getCif());
        dto.setCorreo(e.getCorreo());
        dto.setNombre(e.getNombres());
        dto.setApellido(e.getApellidos());
        dto.setFacultad(e.getFacultad());
        dto.setCarrera(e.getCarrera());
        // añade aquí todos los campos que necesite el front
        return dto;
    }

}
