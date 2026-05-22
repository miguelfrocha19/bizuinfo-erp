package com.bizuinfo.acesso.bean;

import com.bizuinfo.acesso.model.TipoToken;
import com.bizuinfo.acesso.service.LinkMagicoService;
import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.usuario.model.Role;

import com.bizuinfo.web.Paginas;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@Named
@SessionScoped
public class ConsumirTokenBean implements Serializable {

    @Inject
    private UsuarioDAO dao;

    @Inject
    private LinkMagicoService ls;

    @PostConstruct
    public void init() {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String token = ec.getRequestParameterMap().get("token");

        if (token == null || token.isBlank()) {
            redirecionar(Paginas.LOGIN);
            return;
        }

        Optional<Usuario> optional = dao.buscarPorToken(token);

        if (optional.isEmpty()) {
            redirecionar(Paginas.LOGIN);
            return;
        }

        Usuario usuario = optional.get();

        boolean verificacaoValida =
            ls.tokenValido(
                usuario,
                token,
                TipoToken.VERIFICACAO_EMAIL
            );

        boolean recuperacaoValida =
            ls.tokenValido(
                usuario,
                token,
                TipoToken.RECUPERACAO_ACESSO
            );

        if (!verificacaoValida && !recuperacaoValida) {
            redirecionar(Paginas.LOGIN);
            return;
        }

        if (verificacaoValida) {

            usuario.setEmailVerificado(true);

            ls.invalidarToken(
                usuario,
                TipoToken.VERIFICACAO_EMAIL
            );
        }

        if (recuperacaoValida) {

            ls.invalidarToken(
                usuario,
                TipoToken.RECUPERACAO_ACESSO
            );
        }

        dao.salvar(usuario);
        ec.getSessionMap().put("usuario", usuario);
        redirecionar(dashboard(usuario));
    }

    private String dashboard(Usuario usuario) {

        if (usuario.getRole() == Role.ADMIN) {
            return Paginas.DASHBOARD_ADMIN;
        }

        if (usuario.getRole() == Role.GERENTE) {
            return Paginas.DASHBOARD_GERENTE;
        }

        return Paginas.DASHBOARD;
    }

    private void redirecionar(String pagina) {

        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + pagina);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}