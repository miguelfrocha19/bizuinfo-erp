package com.bizuinfo.venda.bean;

import com.bizuinfo.produto.dao.ProdutoDAO;
import com.bizuinfo.produto.model.Produto;
import com.bizuinfo.venda.dto.ProdutoVendaDTO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class VendaBean implements Serializable {

    @Inject
    private ProdutoDAO produtoDAO;

    @Inject
    private CarrinhoBean carrinhoBean;

    private List<ProdutoVendaDTO> produtos;

    @PostConstruct
    public void init() {
        carregarProdutos();
    }

    private void carregarProdutos() {

        produtos = produtoDAO.listarTodos()
                .stream()
                .map(ProdutoVendaDTO::new)
                .collect(Collectors.toList());
    }

    public void adicionarAoCarrinho(
            ProdutoVendaDTO dto
    ) {

        try {

            if (dto.getQuantidade() <= 0) {

                throw new RuntimeException(
                        "Quantidade inválida."
                );
            }

            carrinhoBean.adicionarProduto(
                    dto.getProduto(),
                    dto.getQuantidade()
            );

            dto.setQuantidade(1);

        } catch (Exception e) {

            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Erro",
                                    e.getMessage()
                            )
                    );
        }
    }

    public List<ProdutoVendaDTO> getProdutos() {
        return produtos;
    }

}
