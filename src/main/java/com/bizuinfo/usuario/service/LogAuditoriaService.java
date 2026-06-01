package com.bizuinfo.usuario.service;

import com.bizuinfo.infra.util.RequestUtil;
import com.bizuinfo.usuario.dao.LogAuditoriaDAO;
import com.bizuinfo.usuario.model.LogAuditoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LogAuditoriaService {

    @Inject
    private LogAuditoriaDAO logAuditoriaDAO;

    /**
     * Registra uma ação no log de auditoria
     *
     * @param acao ex: "CADASTRO_USUARIO", "EDICAO_USUARIO"
     * @param detalhe descrição do que aconteceu
     * @param usuarioResponsavel email ou nome do usuário que executou a ação
     */
    public void registrar(String acao,
                          String detalhe,
                          String usuarioResponsavel) {

        String ipOrigem = RequestUtil.getIpUsuario();

        LogAuditoria log = new LogAuditoria(
                acao,
                detalhe,
                usuarioResponsavel,
                ipOrigem
        );

        logAuditoriaDAO.salvar(log);
    }
}