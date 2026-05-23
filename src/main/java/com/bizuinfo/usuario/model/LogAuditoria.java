package com.bizuinfo.usuario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String acao; // Ex: "LOGIN", "LOGOUT"

    @Column(columnDefinition = "TEXT")
    private String detalhe;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    private String usuarioResponsavel;

    public LogAuditoria() {
        this.dataHora = LocalDateTime.now();
    }

    public LogAuditoria(String acao, String detalhe, String usuarioResponsavel) {
        this.acao = acao;
        this.detalhe = detalhe;
        this.usuarioResponsavel = usuarioResponsavel;
        this.dataHora = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getDetalhe() { return detalhe; }
    public void setDetalhe(String detalhe) { this.detalhe = detalhe; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(String usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }
}