package com.bizuinfo.produto.bean;

import com.bizuinfo.produto.dao.CategoriaDAO;
import com.bizuinfo.produto.model.Categoria;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CategoriaBean implements Serializable {

    @Inject
    private CategoriaDAO categoriaDAO;

    private List<Categoria> categorias;

    @PostConstruct
    public void init() {
        categorias = categoriaDAO.listarTodos();
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }
}