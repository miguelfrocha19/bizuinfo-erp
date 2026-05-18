package com.bizuinfo.acesso.service;

import com.bizuinfo.infra.service.EmailService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConfirmarEmailService {

    @Inject
    private EmailService emailService;

    @Inject
    private LinkMagicoService linkMagicoService;

    public void enviarLink(String email) {
        // Pegar toda a lógica de enviar link do ConfirmarEmailBean
    }
}
