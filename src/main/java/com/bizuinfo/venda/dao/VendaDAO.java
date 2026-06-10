package com.bizuinfo.venda.dao;

import com.bizuinfo.infra.dao.GenericoDAO;
import com.bizuinfo.venda.model.Venda;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VendaDAO extends GenericoDAO<Venda> {

    public VendaDAO() {
        super(Venda.class);
    }

}