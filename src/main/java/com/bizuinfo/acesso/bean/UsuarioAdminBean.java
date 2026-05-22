package com.bizuinfo.acesso.bean;

import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.usuario.model.Role;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioDAO usuarioDAO;

    private List<Usuario> usuarios;

    @PostConstruct
    public void init() {
        carregarUsuarios();
    }

    public void carregarUsuarios() {
        usuarios = usuarioDAO.listarTodos();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void salvar(Usuario usuario) {
        usuarioDAO.salvar(usuario);
        carregarUsuarios();
    }
}