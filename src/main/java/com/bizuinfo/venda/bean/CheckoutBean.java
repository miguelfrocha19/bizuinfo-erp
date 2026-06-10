package com.bizuinfo.venda.bean;

import com.bizuinfo.acesso.bean.SessaoBean;
import com.bizuinfo.usuario.model.Usuario;
import com.bizuinfo.venda.model.*;
import com.bizuinfo.venda.service.VendaPDFService;
import com.bizuinfo.venda.service.VendaService;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Named
@ViewScoped
public class CheckoutBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CarrinhoBean carrinhoBean;

    @Inject
    private SessaoBean sessaoBean;

    @EJB
    private VendaPDFService vendaPDFService;

    @EJB
    private VendaService vendaService;

    private FormaPagamento formaPagamento;

    private Venda vendaFinalizada;

    public String finalizar() {

        try {

            if (carrinhoBean.getItens().isEmpty()) {
                throw new RuntimeException("Carrinho está vazio.");
            }

            if (formaPagamento == null) {
                throw new RuntimeException("Selecione uma forma de pagamento.");
            }

            Usuario usuario = sessaoBean.getUsuarioLogado();

            // Cria venda
            Venda venda = new Venda();
            venda.setUsuario(usuario);
            venda.setDataVenda(LocalDateTime.now());

            // Pagamento (único)
            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(formaPagamento);
            pagamento.setStatusPagamento(StatusPagamento.APROVADO);
            pagamento.setValor(carrinhoBean.getValorTotal());

            // Executa venda (regra central no service)
            vendaFinalizada = vendaService.finalizarVenda(
                    venda,
                    pagamento,
                    carrinhoBean.getItens()
            );

            // limpa carrinho após sucesso
            carrinhoBean.limparCarrinho();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Sucesso",
                            "Venda finalizada com sucesso!"
                    )
            );

            // redireciona para recibo
            return "/restrito/venda/recibo_venda.xhtml?faces-redirect=true&vendaId="
                    + vendaFinalizada.getId();

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Erro ao finalizar venda",
                            e.getMessage()
                    )
            );

            return null;
        }
    }

    public FormaPagamento getFormaPagamento() {  return formaPagamento;  }

    public void setFormaPagamento(FormaPagamento formaPagamento) {  this.formaPagamento = formaPagamento;  }

    public Venda getVendaFinalizada() {  return vendaFinalizada;  }
}