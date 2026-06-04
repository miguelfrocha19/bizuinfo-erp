package com.bizuinfo.web;

public class Paginas {

    // Prefixos de acesso
    private static final String RESTRITO = "/restrito/";
    private static final String PUBLICO = "/publico/";

    // Credencial
    private static final String FUNCIONARIO = "funcionario/";
    private static final String GERENTE = "gerente/";
    private static final String ADMIN = "admin/";

    // Módulo
    private static final String ACESSO = "acesso/";
    private static final String APP = "app/";
    private static final String PRODUTO = "produto/";
    private static final String PERFIL = "perfil/";

    // Páginas públicas                  NOME                ACESSO   MÓDULO  CREDENCIAL       PÁGINA
    public static final String LOGIN                      = PUBLICO + ACESSO             + "login.xhtml";
    public static final String CADASTRO                   = PUBLICO + ACESSO             + "cadastro.xhtml";
    public static final String RECUPERAR_ACESSO           = PUBLICO + ACESSO             + "recuperar_acesso.xhtml";
    public static final String CONFIRMAR_EMAIL            = PUBLICO + ACESSO             + "confirmar_email.xhtml";
    public static final String CONSUMIR_TOKEN_CONFIRMACAO = PUBLICO + ACESSO             + "consumir_token_confirmacao.xhtml";
    public static final String CONSUMIR_TOKEN_RECUPERACAO = PUBLICO + ACESSO             + "consumir_token_recuperacao.xhtml";

    // Dashboards                        NOME                ACESSO   MÓDULO  CREDENCIAL       PÁGINA
    public static final String DASHBOARD                  = RESTRITO + APP + FUNCIONARIO + "dashboard.xhtml";
    public static final String DASHBOARD_GERENTE          = RESTRITO + APP + GERENTE     + "dashboard_gerente.xhtml";
    public static final String DASHBOARD_ADMIN            = RESTRITO + APP + ADMIN       + "dashboard_admin.xhtml";

    // Perfil
    public static final String PERFIL_USUARIO             = RESTRITO + APP + PERFIL      + "perfil_usuario.xhtml";

    // Produto                           NOME                ACESSO             MÓDULO         PÁGINA
    public static final String GERENCIAR_PRODUTOS         = RESTRITO + APP + PRODUTO     + "gerenciar_produtos.xhtml";
    public static final String GERENCIAR_CATEGORIAS       = RESTRITO + APP + PRODUTO     + "gerenciar_categorias.xhtml";
    public static final String DETALHES_PRODUTO           = RESTRITO + APP + PRODUTO     + "detalhes_produto.xhtml";


    // Outras                            NOME                ACESSO   MÓDULO  CREDENCIAL       PÁGINA
    public static final String ACESSO_NEGADO              = PUBLICO                      + "acesso_negado.xhtml";


}
