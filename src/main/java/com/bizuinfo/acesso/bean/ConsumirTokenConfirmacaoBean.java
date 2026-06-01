package com.bizuinfo.acesso.bean;

import com.bizuinfo.acesso.model.TipoToken;
import com.bizuinfo.acesso.service.ConsumirTokenConfirmacaoService;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.web.Paginas;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class ConsumirTokenConfirmacaoBean implements Serializable {

    @Inject
    private ConsumirTokenConfirmacaoService consumirTokenService;

    private String token;

    public String consumir() {

        Usuario usuario = consumirTokenService.validarToken(token);

        if (usuario == null) {
            return Paginas.ACESSO_NEGADO + "?faces-redirect=true";
        }

        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("usuario", usuario);

        return Paginas.DASHBOARD + "?faces-redirect=true";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}