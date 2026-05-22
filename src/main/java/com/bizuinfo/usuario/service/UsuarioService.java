package com.bizuinfo.usuario.service;

import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.usuario.model.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioDAO uDAO;

    public boolean cadastrar(String nome, String email, String senha) {

        if (uDAO.buscarPorEmail(email).isPresent()) {
            return false;
        }

        Usuario usuario = new Usuario(
            nome,
            email,
            BCrypt.hashpw(senha, BCrypt.gensalt())
        );

        uDAO.salvar(usuario);

        return true;
    }

    public void alterarPerfil(Long idUsuario, String novaRole) {

        Usuario usuario = uDAO.buscarPorId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + idUsuario));

        Role role = Role.valueOf(novaRole);

        usuario.setRole(role);

        uDAO.salvar(usuario);
    }
}