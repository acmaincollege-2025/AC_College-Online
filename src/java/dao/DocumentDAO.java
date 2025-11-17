/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Documents;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
public class DocumentDAO {

    private Connection conn;

    public DocumentDAO() {
        try {
            conn = DatabaseUtil.getConnection(); // assumes DBUtil manages DB connections
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean saveDocument(Documents doc) {
        String sql = "INSERT INTO documents (student_id, file_name, file_path, file_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doc.getStudentId());
            ps.setString(2, doc.getFileName());
            ps.setString(3, doc.getFilePath());
            ps.setString(4, doc.getFileType());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public List<Documents> getDocumentsByStudentId(String studentId) {
        List<Documents> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE student_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Documents doc = new Documents();
                doc.setDocumentId(rs.getInt("document_id"));
                doc.setStudentId(rs.getString("student_id"));
                doc.setFileName(rs.getString("file_name"));
                doc.setFilePath(rs.getString("file_path"));
                doc.setFileType(rs.getString("file_type"));
                doc.setUploadDate(rs.getTimestamp("upload_date"));
                documents.add(doc);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return documents;
    }

    public boolean deleteDocument(int documentId) {
        String sql = "DELETE FROM documents WHERE document_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, documentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
}
