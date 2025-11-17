/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import model.Payment;

/**
 *
 * @author hrkas
 */
public class ReceiptPDFGenerator {

    public ReceiptPDFGenerator() {
    }

    public static void generateReceipt(Payment payment, HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt.pdf");

        OutputStream out = response.getOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        // Load school logo (make sure path is accessible in your project)
        String logoPath = FacesContext.getCurrentInstance().getExternalContext()
                .getRealPath("/resources/images/logo.png");
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(80, 80);
        logo.setAlignment(Element.ALIGN_LEFT);
        document.add(logo);

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Acknowledgement Payment Receipt", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Info Table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        addCell(table, "Student No:", labelFont);
        addCell(table, payment.getStudentNo(), valueFont);
        addCell(table, "Student Name:", labelFont);
        addCell(table, payment.getStudentName(), valueFont);
        addCell(table, "Enrollment ID:", labelFont);
        addCell(table, String.valueOf(payment.getEnrollmentId()), valueFont);
        addCell(table, "Payment Type:", labelFont);
        addCell(table, payment.getPaymentType(), valueFont);
        addCell(table, "Reference No:", labelFont);
        addCell(table, payment.getPaymentReferenceNumber(), valueFont);
        addCell(table, "Amount Paid:", labelFont);
        addCell(table, String.format("₱ %.2f", payment.getAmountPaid()), valueFont);
        addCell(table, "Payment Date:", labelFont);
        addCell(table, sdf.format(payment.getPaymentDate()), valueFont);

        document.add(table);

        // QR Code
        BarcodeQRCode qr = new BarcodeQRCode(payment.getPaymentReferenceNumber(), 100, 100, null);
        Image qrImage = qr.getImage();
        qrImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrImage);

        Paragraph thanks = new Paragraph("Thank you for your payment!", labelFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(thanks);

        document.close();
        out.flush();
        out.close();
    }

    private static void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }
}
