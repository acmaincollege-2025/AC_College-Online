/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import model.EnrolledCourse;

/**
 *
 * @author hrkas
 */
public class DeanReportUtil {
    public static void generatePDF(OutputStream os, String studentName, List<EnrolledCourse> courseList) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, os);
            document.open();

            // Logo setup
            String logoPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/images/logo.png");
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(80, 80);

            // Title font
            Font schoolNameFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font reportTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font tableFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Header table with logo and school name
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1, 4});

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Paragraph schoolName = new Paragraph("Your College Name", schoolNameFont);
            Paragraph address = new Paragraph("123 Main St, City, Country", tableFont);
            Paragraph reportTitle = new Paragraph("Student Progress Report", reportTitleFont);
            titleCell.addElement(schoolName);
            titleCell.addElement(address);
            titleCell.addElement(reportTitle);

            header.addCell(logoCell);
            header.addCell(titleCell);
            document.add(header);
            document.add(Chunk.NEWLINE);

            // Student name
            document.add(new Paragraph("Student: " + studentName, tableFont));
            document.add(Chunk.NEWLINE);

            // Table of enrolled courses
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 4, 1, 1, 2});

            table.addCell(new PdfPCell(new Phrase("Course Code", tableFont)));
            table.addCell(new PdfPCell(new Phrase("Description", tableFont)));
            table.addCell(new PdfPCell(new Phrase("Units", tableFont)));
            table.addCell(new PdfPCell(new Phrase("Grade", tableFont)));
            table.addCell(new PdfPCell(new Phrase("Remarks", tableFont)));

            for (EnrolledCourse ec : courseList) {
                table.addCell(new PdfPCell(new Phrase(ec.getCourse_code(), tableFont)));
                table.addCell(new PdfPCell(new Phrase(ec.getCourse_description(), tableFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ec.getUnits()), tableFont)));
                table.addCell(new PdfPCell(new Phrase(ec.getGrade() != null ? ec.getGrade() : "", tableFont)));
                table.addCell(new PdfPCell(new Phrase(ec.getRemarks() != null ? ec.getRemarks() : "", tableFont)));
            }

            document.add(table);
            document.close();

        } catch (DocumentException ex) {
            System.out.println("Exception: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(DeanReportUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
