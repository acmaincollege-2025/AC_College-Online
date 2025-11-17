/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hrkas
 */
@ManagedBean
@NoneScoped
public class EnrollmentCertificateBean {

    /**
     * Creates a new instance of EnrollmentCertificateBean
     */
    public EnrollmentCertificateBean() {
    }

    public void generateCertificate(String studentName, String studentNo, String program, String academicYear, String semester) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"enrollment_certificate.pdf\"");

            OutputStream out = response.getOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // Load logo image
            String logoPath = context.getExternalContext().getRealPath("/resources/images/logo.png");
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_LEFT);

            // Header Table: Logo + School Name
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 3f});

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setRowspan(2);

            Font schoolFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font subFont = new Font(Font.FontFamily.HELVETICA, 10);

            PdfPCell schoolCell = new PdfPCell();
            schoolCell.setBorder(Rectangle.NO_BORDER);
            schoolCell.addElement(new Phrase("Your College Name Here", schoolFont));
            schoolCell.addElement(new Phrase("123 University Avenue, City, Province", subFont));
            schoolCell.addElement(new Phrase("www.yourschool.edu.ph | (02) 123-4567", subFont));

            headerTable.addCell(logoCell);
            headerTable.addCell(schoolCell);
            document.add(headerTable);

            // Line separator
            LineSeparator ls = new LineSeparator();
            ls.setOffset(-5);
            document.add(new Chunk(ls));
            document.add(new Paragraph(" "));

            // Certificate title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("ENROLLMENT CONFIRMATION LETTER", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Body content
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12);
            document.add(new Paragraph("This is to certify that:\n", bodyFont));
            document.add(new Paragraph("Name: " + studentName, bodyFont));
            document.add(new Paragraph("Student Number: " + studentNo, bodyFont));
            document.add(new Paragraph("Program: " + program, bodyFont));
            document.add(new Paragraph("Academic Year: " + academicYear, bodyFont));
            document.add(new Paragraph("Semester: " + semester, bodyFont));

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            document.add(new Paragraph("\nHas been officially enrolled as of " + sdf.format(new Date()) + ".", bodyFont));

            document.add(new Paragraph("\n\n\nRegistrar: ___________________________", bodyFont));
            document.add(new Paragraph("\n\nInstitution Name | Address | Website", bodyFont));
// Generate certificate number
            String certificateNumber = "CERT-" + new SimpleDateFormat("yyyy").format(new Date()) + "-" + studentNo;

// Add certificate number
            document.add(new Paragraph("\nCertificate Number: " + certificateNumber, bodyFont));

// QR code (e.g., link to verification page or embed cert number)
            String qrContent = "https://yourschool.edu/verify?cert=" + certificateNumber;
            Image qrCodeImage = generateQRCodeImage(qrContent, 100, 100);
            qrCodeImage.setAlignment(Image.ALIGN_RIGHT);
            document.add(qrCodeImage);

            document.close();
            out.flush();
            out.close();
            context.responseComplete();

        } catch (DocumentException | IOException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            Logger.getLogger(EnrollmentCertificateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Image generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Image.getInstance(pngData);
    }

}
