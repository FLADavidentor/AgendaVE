package com.uam.agendave.service;

import com.uam.agendave.model.Estudiante;
import com.uam.agendave.util.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private static final String BASE_URL = "https://uvirtual.uam.edu.ni:442/uambiblioapi/User/";
    private final RestClient restClient;

    public EstudianteServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public String autenticarEstudiante(String cif, String password) {
        String loginUrl = BASE_URL + "login";

        // Crear el cuerpo de la solicitud
        Map<String, String> requestBody = Map.of(
                "cif", cif,
                "password", password
        );

        try {
            // Realizar la solicitud POST con RestClient
            String token = restClient.method(HttpMethod.POST)
                    .uri(loginUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            return token;

        } catch (HttpClientErrorException e) {
            // Manejo de errores 4xx
            System.err.println("Error HTTP (4xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error de autenticación: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            // Manejo de errores 5xx
            System.err.println("Error HTTP (5xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error en el servidor externo: " + e.getMessage(), e);
        } catch (Exception e) {
            // Manejo de errores generales
            System.err.println("Error inesperado al comunicarse con la API externa: " + e.getMessage());
            throw new RuntimeException("Error desconocido: " + e.getMessage(), e);
        }
    }

    @Override
    public Estudiante obtenerInformacionEstudiante(String cif, String token) {
        String infoUrl = BASE_URL + "GetStudentInformation?cif=" + cif;

        // Verifica si el token ya incluye el prefijo "Bearer"
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        try {
            // Realizar la solicitud GET con RestClient
            ApiResponse apiResponse = restClient.method(HttpMethod.GET)
                    .uri(infoUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .body(ApiResponse.class);

            // Mapear el primer elemento de "data" a un Estudiante
            if (apiResponse != null && apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                return mapearAEstudiante(apiResponse.getData().get(0));
            } else {
                throw new IllegalArgumentException("No se encontraron datos para el CIF proporcionado");
            }

        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP (4xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al obtener información del estudiante: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            System.err.println("Error HTTP (5xx): " + e.getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error en el servidor externo: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error desconocido: " + e.getMessage(), e);
        }
    }

    private Estudiante mapearAEstudiante(Map<String, Object> estudianteData) {
        Estudiante estudiante = new Estudiante();
        estudiante.setCif((String) estudianteData.get("cif"));
        estudiante.setNombres((String) estudianteData.get("nombres"));
        estudiante.setApellidos((String) estudianteData.get("apellidos"));
        estudiante.setTipo((String) estudianteData.get("tipo"));
        estudiante.setCorreo((String) estudianteData.get("correo"));
        estudiante.setSexo((String) estudianteData.get("sexo"));
        estudiante.setTelefono((String) estudianteData.get("telefono"));
        estudiante.setCarrera((String) estudianteData.get("carrera"));
        estudiante.setFacultad((String) estudianteData.get("facultad"));
        return estudiante;
    }

}


