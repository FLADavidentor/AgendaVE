package com.uam.agendave.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final RestClient restClient;
    private static final String EXTERNAL_API_URL = "https://uvirtual.uam.edu.ni:442/uambiblioapi/User/GetStudentInformation";

    public JwtFilter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Extraer el token sin "Bearer "

            try {
                // Realizar una solicitud GET a la API externa con el token
                Map<String, Object> apiResponse = obtenerInformacionEstudiante(jwt);

                if (apiResponse != null && Boolean.TRUE.equals(apiResponse.get("success"))) {
                    // Obtener los datos del estudiante
                    Map<String, Object> estudianteData = (Map<String, Object>) ((Map<?, ?>) apiResponse.get("data")).get(0);
                    String correo = (String) estudianteData.get("correo");

                    // Establecer la autenticación en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(correo, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.err.println("Error al autenticar al estudiante: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    private Map<String, Object> obtenerInformacionEstudiante(String token) {
        return restClient
                .method(HttpMethod.GET)
                .uri(EXTERNAL_API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Map.class);
    }
}

