package com.uam.agendave.service;

import com.uam.agendave.model.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    List<Usuario> obtenerTodos();
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarPorId(UUID id);
    void eliminarUsuario(UUID id);
    List<Usuario> buscarPorNombre(String nombre);
    Usuario buscarPorCorreo(String correo);
}
