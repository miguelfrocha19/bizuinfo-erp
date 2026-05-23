package com.bizuinfo.acesso.bean;

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
public class RecuperarAcessoBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;

    private boolean linkEnviado = false;

    private long ultimoReenvio = 0;
    private static final long INTERVALO = 30000; // 30 segundos

    @Inject
    private UsuarioDAO dao;

    public void enviarLink() {

        ultimoReenvio = System.currentTimeMillis();
        enviarLinkRecuperacao();
        linkEnviado = true;
        FacesContext.getCurrentInstance().addMessage(
        null,
            new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Link enviado",
                "Verifique sua caixa de entrada."
            )
        );
    }

    public void reenviarLink() {

        if (getContadorAtivo()) {
            return;
        }

        ultimoReenvio = System.currentTimeMillis();
        enviarLinkRecuperacao();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Link reenviado",
                        "Verifique sua caixa de entrada."
                )
        );
    }

    private void enviarLinkRecuperacao() {

        Optional<Usuario> optUsuario = dao.buscarPorEmail(email);

        if (optUsuario.isEmpty()) {
            return;
        }

        Usuario usuario = optUsuario.get();
        LinkMagicoService ls = new LinkMagicoService();
        ls.gerarToken(usuario, TipoToken.RECUPERACAO_ACESSO);
        dao.salvar(usuario);

        String link = "http://localhost:8080/"
                    + "bizuinfo_erp_war_exploded/"
                    + "publico/recuperar-acesso.xhtml?token="
                    + usuario.getTokenReset();

        String conteudo = "Clique no link para recuperar acesso:<br><br>"
                        + "<a href='" + link + "'>Recuperar acesso</a>";

        EmailService es = new EmailService();
        es.enviarEmail(usuario.getEmail(), "Recuperação de acesso", conteudo);
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
    public boolean isLinkEnviado() {
        return linkEnviado;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setLinkEnviado(boolean linkEnviado) {
        this.linkEnviado = linkEnviado;
    }
}