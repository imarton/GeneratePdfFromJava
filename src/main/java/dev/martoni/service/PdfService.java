package dev.martoni.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import dev.martoni.model.Asset;
import dev.martoni.model.DocumentData;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PdfService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd. HH:mm");

    public byte[] generatePdf(DocumentData data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Betűtípusok betöltése (magyar ékezetek támogatásához)
            String fontPath = getClass().getResource("/arial.ttf").getPath();
            String boldFontPath = getClass().getResource("/arialbd.ttf").getPath();
            String faFontPath = getClass().getResource("/font_awesome_7_Solid-900.otf").getPath();

            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bfBold = BaseFont.createFont(boldFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bfFa = BaseFont.createFont(faFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font titleFont = new Font(bfBold, 20);
            Font normalFont = new Font(bf, 10);
            Font smallFont = new Font(bf, 9);
            Font tableHeaderFont = new Font(bfBold, 9, Font.NORMAL, Color.WHITE);
            Font tableCellFont = new Font(bf, 8);
            Font boldSmallFont = new Font(bfBold, 9);
            Font faFont = new Font(bfFa, 24, Font.NORMAL, new Color(60, 118, 61));

            // Cím
            Paragraph title = new Paragraph("Informatikai eszközmozgatási bizonylat", titleFont);
            document.add(title);

            // Alapadatok
            document.add(new Paragraph("Bizonylat azonosító: " + data.getId(), normalFont));
            document.add(new Paragraph("Kiállítás ideje: " + DATE_FORMAT.format(data.getIssueDate()), normalFont));
            document.add(new Paragraph("Kiállította: " + data.getIssuedBy(), normalFont));

            document.add(Chunk.NEWLINE);

            // Táblázat
            PdfPTable table = new PdfPTable(new float[]{15, 15, 15, 20, 15, 20});
            table.setWidthPercentage(100);

            // Táblázat fejléce
            PdfPCell mainHeader = new PdfPCell(new Phrase("Átvett eszközök", tableHeaderFont));
            mainHeader.setColspan(6);
            mainHeader.setBackgroundColor(new Color(52, 73, 94));
            mainHeader.setPadding(5);
            table.addCell(mainHeader);

            table.addCell(createHeaderCell("Kategória", smallFont));
            table.addCell(createHeaderCell("Leltári szám", smallFont));
            table.addCell(createHeaderCell("Márkanév", smallFont));
            table.addCell(createHeaderCell("Modell", smallFont));
            table.addCell(createHeaderCell("Gyári szám", smallFont));
            table.addCell(createHeaderCell("SM azonosító", smallFont));

            // Eszközök
            for (Asset asset : data.getAssets()) {
                table.addCell(createCell(asset.getCategory(), tableCellFont));
                table.addCell(createCell(asset.getInventoryNumber(), tableCellFont));
                table.addCell(createCell(asset.getBrand(), tableCellFont));
                table.addCell(createCell(asset.getModel(), tableCellFont));
                table.addCell(createCell(asset.getSerialNumber(), tableCellFont));
                table.addCell(createCell(asset.getSmId(), tableCellFont));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Felelősök és helyszínek
            document.add(createLabeledParagraph("Előző felelős: ", data.getPreviousOwner(), boldSmallFont, smallFont));
            document.add(createLabeledParagraph("Előző hely: ", data.getPreviousLocation(), boldSmallFont, smallFont));
            document.add(createLabeledParagraph("Új felelős: ", data.getNewOwner(), boldSmallFont, smallFont));
            document.add(createLabeledParagraph("Új hely: ", data.getNewLocation(), boldSmallFont, smallFont));

            document.add(Chunk.NEWLINE);
            Paragraph note = new Paragraph("A személyre átvett eszközök visszaszolgáltatási, illetve elszámolási kötelezettséggel kerülnek átvételre!", 
                    new Font(bfBold, 10));
            document.add(note);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Aláírás blokk (átadó / átvevő)
            DottedLineSeparator dotted = new DottedLineSeparator();
            dotted.setLineWidth(1f);
            dotted.setGap(2f);

            PdfPTable datumTable = buildDateTable(smallFont, dotted, "Dátum:");
            document.add(datumTable);

//            document.add(Chunk.NEWLINE);
            PdfPTable signTable = new PdfPTable(new float[]{50, 50});
            signTable.setWidthPercentage(100);
            PdfPCell left = buildSignTableCell(dotted, boldSmallFont, smallFont, "Átadó", data.getPreviousOwner() == null ? "" : data.getPreviousOwner());
            PdfPCell right = buildSignTableCell(dotted, boldSmallFont, smallFont, "Átvevő", data.getNewOwner() == null ? "" : data.getNewOwner());
            signTable.addCell(left);
            signTable.addCell(right);
            document.add(signTable);

            document.close();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }

        byte[] bytes = baos.toByteArray();
        //saveToFile(bytes, data.getId());
        return bytes;
    }

    private PdfPTable buildDateTable(Font smallFont, DottedLineSeparator dotted, String textValue) {
        PdfPTable dateTable = new PdfPTable(new float[]{20, 80});
        dateTable.setTotalWidth(200f);
        dateTable.setLockedWidth(true);
        dateTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell dateLabelCell = new PdfPCell(new Phrase(textValue, smallFont));
        dateLabelCell.setBorder(Rectangle.NO_BORDER);
        dateLabelCell.setPadding(0);

        PdfPCell dateLineCell = new PdfPCell();
        dateLineCell.setBorder(Rectangle.NO_BORDER);
        dateLineCell.setPadding(0);
        dateLineCell.addElement(new Chunk(dotted));

        dateTable.addCell(dateLabelCell);
        dateTable.addCell(dateLineCell);

        return dateTable;
    }

    private static PdfPCell buildSignTableCell(DottedLineSeparator dotted, Font boldSmallFont, Font smallFont, String title, String name) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(30);
        cell.setPaddingLeft(50);
        cell.setPaddingRight(50);
//        cell.setMinimumHeight(90);

        Paragraph leftSignLine = new Paragraph();
        leftSignLine.setAlignment(Element.ALIGN_CENTER);
        leftSignLine.add(new Chunk(dotted));
        Paragraph leftTitle = new Paragraph(title, boldSmallFont);
        leftTitle.setAlignment(Element.ALIGN_CENTER);
        Paragraph leftName = new Paragraph(name, smallFont);
        leftName.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(leftSignLine);
        cell.addElement(leftTitle);
        cell.addElement(leftName);

        return cell;
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setPadding(4);
        return cell;
    }

    private Paragraph createLabeledParagraph(String label, String value, Font labelFont, Font valueFont) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label, labelFont));
        p.add(new Chunk(value, valueFont));
        return p;
    }

    private void saveToFile(byte[] bytes, String id) {
        String fileName = "bizonylat_" + id.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
            System.out.println("PDF elmentve: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
