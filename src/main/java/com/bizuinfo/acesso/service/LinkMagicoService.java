package com.bizuinfo.acesso.service;

import com.bizuinfo.acesso.model.TipoToken;
import com.bizuinfo.usuario.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class LinkMagicoService {

    private static final int EXPIRACAO_MINUTOS = 15;

    public void gerarToken(Usuario usuario, TipoToken tipo) {

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS);

        switch (tipo) {

            case VERIFICACAO_EMAIL:
                usuario.setTokenVerificacao(token);
                usuario.setTokenVerificacaoExpiracao(expiracao);
                break;

            case RECUPERACAO_ACESSO:
                usuario.setTokenRecuperacao(token);
                usuario.setTokenRecuperacaoExpiracao(expiracao);
                break;
        }

        usuario.setUltimoEnvioToken(LocalDateTime.now());
    }

    public boolean tokenValido(Usuario u, String token, TipoToken t) {

        if (token == null) {
            return false;
        }

        return switch (t) {

            case VERIFICACAO_EMAIL ->
                token.equals(u.getTokenVerificacao())
                    && tokenNaoExpirado(u.getTokenVerificacaoExpiracao());

            case RECUPERACAO_ACESSO ->
                token.equals(u.getTokenRecuperacao())
                    && tokenNaoExpirado(u.getTokenRecuperacaoExpiracao());
        };
    }

    public void invalidarToken(Usuario usuario, TipoToken tipo) {
        switch (tipo) {

            case VERIFICACAO_EMAIL:
                usuario.setTokenVerificacao(null);
                usuario.setTokenVerificacaoExpiracao(null);
                break;

            case RECUPERACAO_ACESSO:
                usuario.setTokenRecuperacao(null);
                usuario.setTokenRecuperacaoExpiracao(null);
                break;
        }
    }

    private boolean tokenNaoExpirado(LocalDateTime expiracao) {
        return expiracao != null && !LocalDateTime.now().isAfter(expiracao);
    }
}