package com.uam.agendave.service;

import com.uam.agendave.dto.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodos();
    UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO buscarPorId(UUID id);
    void eliminarUsuario(UUID id);
    List<UsuarioDTO> buscarPorNombre(String nombre);
    UsuarioDTO buscarPorCorreo(String correo);
    String autenticarUsuario(String username, String password);
}
