package com.uam.agendave.service;

import com.uam.agendave.dto.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {


    String autenticarUsuario(String username, String password);
}
