package com.bizuinfo.acesso.bean;

import com.bizuinfo.acesso.service.ConfirmarEmailService;
import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.acesso.model.TipoToken;
import com.bizuinfo.infra.service.EmailService;

import com.bizuinfo.acesso.service.LinkMagicoService;
import com.bizuinfo.usuario.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Named
@SessionScoped
public class ConfirmarEmailBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;

    private long ultimoReenvio = 0;
    private static final long INTERVALO = 5000;

    @Inject
    ConfirmarEmailService confirmarEmailService;

    public void confirmarEmail() {
        confirmarEmailService.enviarLink(email);
    }

    public void reenviarLink() {

        if (getContadorAtivo()) {
            return;
        }

        ultimoReenvio = System.currentTimeMillis();

        confirmarEmailService.enviarLink(email);

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Email reenviado",
                "Verifique sua caixa de entrada."
            )
        );
    }

    public int getSegundosRestantes() {
        long restante = INTERVALO - (System.currentTimeMillis() - ultimoReenvio);
        return Math.max(0, (int) (restante / 1000));
    }

    public boolean getContadorAtivo() {
        return (System.currentTimeMillis() - ultimoReenvio) < INTERVALO;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}