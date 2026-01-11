package com.platera.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.platera.model.Order;
import com.platera.model.OrderItem;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class PdfService {

    public byte[] generateReceipt(Order order) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A6);
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph title = new Paragraph(order.getRestaurant().getName(), headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            document.add(new Paragraph("Zamówienie #" + order.getId(), normalFont));
            document.add(new Paragraph("Data: " + order.getCreatedAt().toLocalDate(), normalFont));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            for (OrderItem item : order.getItems()) {
                table.addCell(new Phrase(item.getName() + " x" + item.getQuantity(), normalFont));
                table.addCell(new Phrase(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())) + " PLN", normalFont));
            }
            document.add(table);

            document.add(new Paragraph("\n"));
            Paragraph total = new Paragraph("SUMA: " + order.getTotalPrice() + " PLN", headerFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
    }
}