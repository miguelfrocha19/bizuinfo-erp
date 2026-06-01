package com.bizuinfo.acesso.bean;

import com.bizuinfo.usuario.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped // Agora ele persiste enquanto a sessão durar
public class SessaoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Usuario usuarioLogado;

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}