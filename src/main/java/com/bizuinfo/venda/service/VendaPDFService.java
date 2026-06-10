package com.bizuinfo.venda.service;

import com.bizuinfo.venda.model.Venda;
import com.bizuinfo.venda.model.ItemVenda;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.ejb.Stateless;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Stateless
public class VendaPDFService {

    public byte[] gerarRecibo(Venda venda) {

        try {

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // ===== TÍTULO =====
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("RECIBO DE VENDA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // ===== DADOS GERAIS =====
            Font normal = new Font(Font.HELVETICA, 12);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            document.add(new Paragraph(
                    "ID Venda: " + venda.getId(),
                    normal
            ));

            document.add(new Paragraph(
                    "Data: " + venda.getDataVenda().format(formatter),
                    normal
            ));

            document.add(new Paragraph(
                    "Cliente/Usuário: " + venda.getUsuario().getNome(),
                    normal
            ));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("ITENS", titleFont));
            document.add(new Paragraph(" "));

            // ===== TABELA =====
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            addHeader(table, "Produto");
            addHeader(table, "Qtd");
            addHeader(table, "Unitário");
            addHeader(table, "Subtotal");

            double total = 0.0;

            for (ItemVenda item : venda.getItens()) {

                table.addCell(item.getProduto().getNome());
                table.addCell(String.valueOf(item.getQuantidade()));
                table.addCell(String.format("%.2f", item.getValorUnitario()));
                table.addCell(String.format("%.2f", item.getSubtotal()));

                total += item.getSubtotal();
            }

            document.add(table);

            document.add(new Paragraph(" "));

            // ===== TOTAL DESTACADO =====
            Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD);

            Paragraph totalParagraph = new Paragraph(
                    "TOTAL: R$ " + String.format("%.2f", total),
                    totalFont
            );

            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF da venda", e);
        }
    }

    private void addHeader(PdfPTable table, String text) {

        Font font = new Font(Font.HELVETICA, 12, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}