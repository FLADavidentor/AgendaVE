package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.*;
import com.uam.agendave.repository.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final NombreActividadRepository nombreActividadRepository;
    private final NombreActividadService nombreActividadService;
    private final LugarRepository lugarRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImageRepository imagenRepository;


    public ActividadServiceImpl(
            ActividadRepository actividadRepository,
            NombreActividadRepository nombreActividadRepository,
            LugarRepository lugarRepository,
            UsuarioRepository usuarioRepository, ImageRepository imageRepository) {
        this.actividadRepository = actividadRepository;
        this.nombreActividadRepository = nombreActividadRepository;
        this.nombreActividadService=new NombreActividadServiceImpl(nombreActividadRepository);
        this.lugarRepository = lugarRepository;
        this.usuarioRepository = usuarioRepository;
        this.imagenRepository = imageRepository;
    }

    @Override
    public List<ActividadDTO> obtenerTodas() {
        // Obtener todas las actividades desde el repositorio
        List<Actividad> actividades = actividadRepository.findAll();

        // Convertir las entidades a DTOs
        return actividades.stream().map(actividad -> {
            ActividadDTO actividadDTO = new ActividadDTO();
            actividadDTO.setId(actividad.getId());
            actividadDTO.setDescripcion(actividad.getDescripcion());
            actividadDTO.setFecha(actividad.getFecha());
            actividadDTO.setHoraInicio(actividad.getHoraInicio());
            actividadDTO.setHoraFin(actividad.getHoraFin());
            actividadDTO.setEstado(actividad.isEstado());
            actividadDTO.setCupo(actividad.getCupo());

            // Manejar las relaciones: convertir IDs a nombres
            actividadDTO.setNombreActividad(actividad.getNombreActividad() != null
                    ? actividad.getNombreActividad().getNombre()
                    : "Nombre no especificado");
            actividadDTO.setLugar(actividad.getLugar() != null
                    ? actividad.getLugar().getNombre()
                    : "Lugar no especificado");

            // Manejar convalidaciones
            actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
            actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

            // Información de la imagen asociada
            if (actividad.getImage() != null) {
                ImageData image = actividad.getImage();
                actividadDTO.setImageName(image.getName());
                actividadDTO.setImageType(image.getType());
                actividadDTO.setImageBase64(Base64.getEncoder().encodeToString(image.getImageData()));
            }

            return actividadDTO;
        }).collect(Collectors.toList());
    }


    @Override
    public void guardarActividad(ActividadDTO actividadDTO) throws Exception {
        try {
            // Buscar o crear NombreActividad
            NombreActividad nombreActividad = nombreActividadRepository.findByNombre(actividadDTO.getNombreActividad())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        NombreActividad nuevaNombreActividad = new NombreActividad();
                        nuevaNombreActividad.setNombre(actividadDTO.getNombreActividad());
                        return nombreActividadService.guardar(nuevaNombreActividad); // Usar el servicio para persistir
                    });
            System.out.println(nombreActividad.getId());

            // Buscar Lugar
            Lugar lugar = lugarRepository.findByNombreContainingIgnoreCase(actividadDTO.getLugar())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar()));
            System.out.println(lugar.getId());

            // Procesar la imagen en formato Base64
            String base64Data = actividadDTO.getImageBase64();
            if (base64Data == null || base64Data.isEmpty()) {
                throw new IllegalArgumentException("El campo 'imageBase64' no puede ser nulo o vacío.");
            }

            // Decodificar Base64
            if (base64Data.startsWith("data:image")) {
                base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Crear o buscar la imagen
            ImageData imageData = imagenRepository.findByName(actividadDTO.getImageName())
                    .orElseGet(() -> {
                        ImageData nuevaImageData = new ImageData();
                        nuevaImageData.setName(actividadDTO.getImageName());
                        nuevaImageData.setType(actividadDTO.getImageType());
                        nuevaImageData.setImageData(imageBytes);
                        return imagenRepository.save(nuevaImageData); // Guardar la imagen
                    });

            // Crear la actividad
            Actividad actividad = new Actividad();
            actividad.setNombre(nombreActividad.getNombre());
            actividad.setDescripcion(actividadDTO.getDescripcion());
            actividad.setFecha(actividadDTO.getFecha());
            actividad.setHoraInicio(actividadDTO.getHoraInicio());
            actividad.setHoraFin(actividadDTO.getHoraFin());
            actividad.setCupo(actividadDTO.getCupo());
            actividad.setNombreActividad(nombreActividad);
            actividad.setLugar(lugar);
            actividad.setImage(imageData); // Relacionar con la imagen

            actividad.setConvalidacionesPermitidas(actividadDTO.getConvalidacionesPermitidas());
            actividad.setTotalConvalidacionesPermitidas(actividadDTO.getTotalConvalidacionesPermitidas());

            // Guardar la actividad
            actividadRepository.save(actividad);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la actividad: " + e.getMessage(), e);
        }
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

    private Actividad convertirAEntidad(ActividadDTO actividadDTO, NombreActividad nombreActividad, Lugar lugar, Usuario usuario) {
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

        // Convalidaciones
        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());
        // Información de la imagen
        if (actividad.getImage() != null) {
            ImageData image = actividad.getImage();
            actividadDTO.setImageName(image.getName());
            actividadDTO.setImageType(image.getType());
            actividadDTO.setImageBase64(Base64.getEncoder().encodeToString(image.getImageData()));
        }

        return actividadDTO;
    }

}
