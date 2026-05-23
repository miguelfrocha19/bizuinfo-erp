package com.bizuinfo.acesso.bean;

import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Role;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.usuario.service.LogAuditoriaService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class UsuarioAdminBean implements Serializable {

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private LogAuditoriaService logAuditoriaService;

    @Inject
    private LoginBean loginBean;

    private int abaAtiva = 0;
    private Long idUsuarioSelecionado;
    private List<Usuario> usuariosCache;

    public void irParaDashboard() {
        abaAtiva = 0;
    }

    public void irParaUsuarios() {
        abaAtiva = 1;
    }

    public void irParaLogs() {
        abaAtiva = 2;
    }

    public int getAbaAtiva() {
        return abaAtiva;
    }

    public List<Usuario> getUsuarios() {
        if (usuariosCache == null) {
            try {
                usuariosCache = usuarioDAO.listarTodos();
            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Erro ao carregar usuários: " + e.getMessage(), null)
                );
                usuariosCache = Collections.emptyList();
            }
        }
        return usuariosCache;
    }

    public void salvar(Usuario usuario) {
        try {
            if (usuario == null) return;

            // evita remover ADMIN de si mesmo
            if (loginBean.getUsuarioLogado() != null &&
                    loginBean.getUsuarioLogado().getId().equals(usuario.getId()) &&
                    usuario.getRole() != Role.ADMIN) {

                usuario.setRole(Role.ADMIN);
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Você não pode remover seu próprio ADMIN", null)
                );
                return;
            }

            usuarioDAO.salvar(usuario);

            logAuditoriaService.registrar(
                    "EDITAR_USUARIO",
                    "Alterou usuário: " + usuario.getEmail() + " | Role: " + usuario.getRole(),
                    loginBean.getUsuarioLogado().getNome()
            );

            usuariosCache = usuarioDAO.listarTodos();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Usuário salvo com sucesso", null)
            );

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao salvar usuário: " + e.getMessage(), null)
            );
        }
    }

    public void alternarAtivo(Usuario usuario) {
        try {
            if (usuario == null) return;

            boolean statusAtual = usuario.getEmailVerificado();
            usuario.setEmailVerificado(!statusAtual);

            usuarioDAO.salvar(usuario);

            logAuditoriaService.registrar(
                    "ALTERAR_STATUS",
                    "Alterou status do usuário ID: " + usuario.getId() + " para " + (usuario.getEmailVerificado() ? "ATIVO" : "INATIVO"),
                    loginBean.getUsuarioLogado().getNome()
            );

            usuariosCache = usuarioDAO.listarTodos();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Status alterado com sucesso", null)
            );

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao alterar status: " + e.getMessage(), null)
            );
        }
    }

    public void prepararExclusao(Usuario usuario) {
        if (usuario != null) {
            idUsuarioSelecionado = usuario.getId();
        }
    }

    public void excluirSelecionado() {
        try {
            if (idUsuarioSelecionado == null) return;

            if (loginBean.getUsuarioLogado() != null &&
                    loginBean.getUsuarioLogado().getId().equals(idUsuarioSelecionado)) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Você não pode excluir seu próprio usuário", null)
                );
                return;
            }

            usuarioDAO.remover(idUsuarioSelecionado);

            logAuditoriaService.registrar(
                    "EXCLUIR_USUARIO",
                    "Excluiu usuário ID: " + idUsuarioSelecionado,
                    loginBean.getUsuarioLogado().getNome()
            );

            idUsuarioSelecionado = null;
            usuariosCache = usuarioDAO.listarTodos();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Usuário removido com sucesso", null)
            );

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao excluir usuário: " + e.getMessage(), null)
            );
        }
    }
}