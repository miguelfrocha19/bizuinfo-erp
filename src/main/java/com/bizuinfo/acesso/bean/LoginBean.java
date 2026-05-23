package com.bizuinfo.acesso.bean;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Role;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.usuario.service.LogAuditoriaService;
import com.bizuinfo.web.Paginas;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private String mensagemLogin;
    private Usuario usuarioLogado;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private LogAuditoriaService logAuditoriaService;

    public String entrar() {

        Optional<Usuario> usuarioOptional = usuarioDAO.buscarPorEmail(email);

        if (usuarioOptional.isEmpty()) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Email não cadastrado"));
            return null;
        }

        Usuario usuario = usuarioOptional.get();

        boolean senhaCorreta;
        try {
            senhaCorreta = BCrypt.checkpw(senha, usuario.getSenha());
        } catch (Exception e) {
            senhaCorreta = false;
        }

        if (!senhaCorreta) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Senha inválida"));

            logAuditoriaService.registrar(
                    "LOGIN_FALHA",
                    "Tentativa de login com senha inválida: " + email,
                    email
            );

            return null;
        }

        this.usuarioLogado = usuario;

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("usuario", usuario);

        logAuditoriaService.registrar(
                "LOGIN",
                "Usuário logou no sistema: " + usuario.getEmail(),
                usuario.getNome()
        );

        if (usuario.getRole() == Role.ADMIN) {
            return "/restrito/app/admin/dashboard_admin.xhtml?faces-redirect=true";
        }

        if (usuario.getRole() == Role.GERENTE) {
            return "/restrito/app/gerente/dashboard_gerente.xhtml?faces-redirect=true";
        }

        return "/restrito/app/funcionario/dashboard.xhtml?faces-redirect=true";
    }

    public String sair() {

        logAuditoriaService.registrar(
                "LOGOUT",
                "Usuário saiu do sistema: " + (usuarioLogado != null ? usuarioLogado.getEmail() : "desconhecido"),
                (usuarioLogado != null ? usuarioLogado.getNome() : "anon")
        );

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        return Paginas.LOGIN + "?faces-redirect=true";
    }

    public boolean logado() {
        return usuarioLogado != null;
    }

    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setUsuarioLogado(Usuario u) { this.usuarioLogado = u; }

    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public String getMensagemLogin() { return mensagemLogin; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
}