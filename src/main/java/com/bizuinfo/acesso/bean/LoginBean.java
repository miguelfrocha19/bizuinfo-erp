package com.bizuinfo.acesso.bean;

import com.bizuinfo.acesso.service.LoginService;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.web.Paginas;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;

    private Usuario usuarioLogado;

    @Inject
    private LoginService loginService;

    public String entrar() {

        Usuario usuario = loginService.autenticar(email, senha);

        if (usuario == null) {
            return null;
        }

        usuarioLogado = usuario;

        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("usuario", usuario);

        return switch (usuario.getRole()) {

            case ADMIN ->
                    Paginas.DASHBOARD_ADMIN + "?faces-redirect=true";

            case GERENTE ->
                    Paginas.DASHBOARD_GERENTE + "?faces-redirect=true";

            default ->
                    Paginas.DASHBOARD + "?faces-redirect=true";
        };
    }

    public String sair() {

        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .invalidateSession();

        return Paginas.LOGIN + "?faces-redirect=true";
    }

    public boolean logado() {
        return usuarioLogado != null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}