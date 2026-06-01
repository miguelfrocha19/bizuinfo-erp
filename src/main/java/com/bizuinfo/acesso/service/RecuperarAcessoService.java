package com.bizuinfo.acesso.service;

import com.bizuinfo.acesso.model.TipoToken;
import com.bizuinfo.infra.service.EmailService;
import com.bizuinfo.usuario.dao.UsuarioDAO;
import com.bizuinfo.usuario.model.Usuario;

import com.bizuinfo.web.Paginas;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class RecuperarAcessoService {

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private LinkMagicoService linkMagicoService;

    @Inject
    private EmailService emailService;

    public void enviarLink(String email) {

        Optional<Usuario> optUsuario =
                usuarioDAO.buscarPorEmail(email);

        if (optUsuario.isEmpty()) {
            return;
        }

        Usuario usuario = optUsuario.get();

        linkMagicoService.gerarToken(
                usuario,
                TipoToken.RECUPERACAO_ACESSO
        );

        usuarioDAO.salvar(usuario);

        String link = "http://localhost:8080/"
                    + "bizuinfo_erp_war_exploded"
                    + Paginas.CONSUMIR_TOKEN_RECUPERACAO + "?token="
                    + usuario.getTokenReset();

        String conteudo = "Clique no link para recuperar acesso:<br><br>"
                        + "<a href='" + link + "'>Recuperar acesso</a>";

        emailService.enviarEmail(
                usuario.getEmail(),
                "Recuperação de acesso",
                conteudo
        );
    }
}