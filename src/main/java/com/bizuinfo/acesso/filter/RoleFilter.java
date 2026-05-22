package com.bizuinfo.acesso.filter;

import com.bizuinfo.usuario.model.Role;
import com.bizuinfo.usuario.model.Usuario;

import com.bizuinfo.web.Paginas;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/restrito/app/*")
public class RoleFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null) {

            res.sendRedirect(req.getContextPath() + Paginas.LOGIN);
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {

            res.sendRedirect(req.getContextPath() + Paginas.LOGIN);
            return;
        }

        String uri = req.getRequestURI();

        boolean adminArea = uri.contains("/admin/");
        boolean gerenteArea = uri.contains("/gerente/");
        boolean funcionarioArea = uri.contains("/funcionario/");

        Role role = usuario.getRole();

        if (adminArea && !role.temPermissao(Role.ADMIN)) {
            acessoNegado(req, res);
            return;
        }

        if (gerenteArea && !role.temPermissao(Role.GERENTE)) {
            acessoNegado(req, res);
            return;
        }

        if (funcionarioArea && !role.temPermissao(Role.FUNCIONARIO)) {
            acessoNegado(req, res);
            return;
        }

        chain.doFilter(request, response);
    }

    private void acessoNegado(
        HttpServletRequest req,
        HttpServletResponse res
    ) throws IOException {
        res.sendRedirect(req.getContextPath() + "/publico/acesso_negado.xhtml");
    }
}