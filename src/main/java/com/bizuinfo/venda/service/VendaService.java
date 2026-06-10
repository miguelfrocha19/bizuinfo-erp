package com.bizuinfo.venda.service;

import com.bizuinfo.infra.util.JPAutil;
import com.bizuinfo.produto.model.Produto;
import com.bizuinfo.usuario.service.LogAuditoriaService;
import com.bizuinfo.venda.model.ItemVenda;
import com.bizuinfo.venda.model.Pagamento;
import com.bizuinfo.venda.model.Venda;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import java.util.List;

@Stateless
public class VendaService {

    @EJB
    private LogAuditoriaService logAuditoriaService;

    public Venda finalizarVenda(
            Venda venda,
            Pagamento pagamento,
            List<ItemVenda> itens
    ) {

        EntityManager em = JPAutil.getEntityManager();

        try {

            em.getTransaction().begin();

            double valorTotal = 0.0;

            for (ItemVenda item : itens) {

                Produto produto = em.find(
                        Produto.class,
                        item.getProduto().getId()
                );

                if (produto == null) {
                    throw new RuntimeException(
                            "Produto não encontrado."
                    );
                }

                if (produto.getEstoqueAtual()
                        < item.getQuantidade()) {

                    throw new RuntimeException(
                            "Estoque insuficiente para: "
                                    + produto.getNome()
                    );
                }

                double subtotal =
                        item.getQuantidade()
                                * produto.getPreco();

                item.setValorUnitario(
                        produto.getPreco()
                );

                item.setSubtotal(
                        subtotal
                );

                valorTotal += subtotal;

                produto.setEstoqueAtual(
                        produto.getEstoqueAtual()
                                - item.getQuantidade()
                );

                em.merge(produto);

            }

            venda.setValorTotal(valorTotal);

            em.persist(venda);

            em.flush();

            for (ItemVenda item : itens) {

                item.setVenda(venda);

                em.persist(item);
            }

            pagamento.setVenda(venda);

            em.persist(pagamento);

            em.getTransaction().commit();

            logAuditoriaService.registrar(
                    "VENDA_FINALIZADA",
                    "Venda ID " + venda.getId() +
                            " valor total " + venda.getValorTotal() +
                            " itens " + itens.size(),
                    venda.getUsuario().getEmail()
            );

            return venda;

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "Erro ao finalizar venda.",
                    e
            );

        } finally {

            em.close();
        }
    }
}